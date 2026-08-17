package com.ead.payment.services.impl;

import com.ead.payment.dtos.PaymentCommandDTO;
import com.ead.payment.dtos.PaymentDTO;
import com.ead.payment.dtos.PaymentEventDTO;
import com.ead.payment.dtos.PaymentRequestDTO;
import com.ead.payment.enums.PaymentControl;
import com.ead.payment.enums.PaymentStatus;
import com.ead.payment.models.CreditCard;
import com.ead.payment.models.Payment;
import com.ead.payment.models.User;
import com.ead.payment.publishers.PaymentCommandPublisher;
import com.ead.payment.publishers.PaymentEventPublisher;
import com.ead.payment.repositories.CreditCardRepository;
import com.ead.payment.repositories.PaymentRepository;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.PaymentService;
import com.ead.payment.services.exceptions.BadRequestException;
import com.ead.payment.services.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentCommandPublisher paymentCommandPublisher;

    @Autowired
    private PaymentStripeServiceImpl paymentStripeServiceImpl;

    @Autowired
    private PaymentEventPublisher paymentEventPublisher;

    @Transactional(readOnly = true)
    @Override
    public Page<PaymentDTO> findAllPaged(Specification<Payment> spec, Pageable pageable) {

        Page<Payment> page = repository.findAll(spec, pageable);
        if(page.isEmpty()){
            throw new ResourceNotFoundException("Payment not found");
        }
        return page.map(PaymentDTO::new);
    }

    @Transactional(readOnly = true)
    @Override
    public PaymentDTO findById(UUID id, UUID userId) {

        Optional<Payment> obj = repository.findByIdAndUserId(id, userId);

        Payment entity = obj.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return new PaymentDTO(entity);
    }

    @Transactional
    @Override
    public PaymentDTO requestPayment(UUID id, PaymentRequestDTO dto) {

        log.debug("Request Payment UUID userId received: {}, payment value received: {} ", id, dto.getValuePaid());

        CreditCard creditCard = new CreditCard();

        Optional<User> obj = userRepository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("User not found" + id));

        Optional<CreditCard> optCard =
                creditCardRepository.findByCreditCardNumber(dto.getCreditCardNumber());

        if (optCard.isPresent()) {

            CreditCard existing = optCard.get();

            if (!existing.getUser().getId().equals(id)) {
                throw new BadRequestException(
                        "This credit card is already associated with another user."
                );
            }
        }

            Optional<CreditCard> opt = creditCardRepository.findByUserId(id);

            if (opt.isPresent()) {
                creditCard = opt.get();
            }

            creditCard.setUser(user);
            copyDtoToEntity(creditCard, dto);

            Optional<Payment> lastPaymentOpt = repository.findTopByUserIdOrderByPaymentRequestDateDesc(id);
            if (lastPaymentOpt.isPresent()) {
                Payment lastPayment = lastPaymentOpt.get();
                if (lastPayment.getPaymentControl().equals(PaymentControl.REQUESTED)) {
                    throw new BadRequestException("There is already a pending payment request for this user.");
                }
                if (lastPayment.getPaymentControl().equals(PaymentControl.EFFECTED) &&
                        lastPayment.getPaymentExpirationDate().isAfter(LocalDateTime.now(ZoneId.of("UTC")))) {
                    throw new BadRequestException("The last payment request for this user has already been approved.");
                }

            }

            Payment payment = new Payment();
            payment.setPaymentControl(PaymentControl.REQUESTED);
            payment.setPaymentRequestDate(LocalDateTime.now(ZoneId.of("UTC")));
            payment.setPaymentExpirationDate(LocalDateTime.now(ZoneId.of("UTC")).plusDays(30));

            String cardNumber = dto.getCreditCardNumber().replaceAll("\\s+", "");

            String lastDigits = cardNumber.substring(cardNumber.length() - 4);

            payment.setLastDigitsCreditCard(lastDigits);

            log.debug("Card number length: {}", dto.getCreditCardNumber().length());
            log.debug("Last four digits: {}", lastDigits);

            payment.setValuePaid(dto.getValuePaid());
            payment.setUser(user);

            creditCardRepository.save(creditCard);
            repository.save(payment);

            try{
                PaymentCommandDTO paymentCommandDTO = new PaymentCommandDTO();
                paymentCommandDTO.setPaymentId(payment.getId());
                paymentCommandDTO.setUserId(user.getId());
                paymentCommandDTO.setCardId(creditCard.getId());

                paymentCommandPublisher.publishPaymentCommand(paymentCommandDTO);

            }catch (Exception e){
                throw new BadRequestException("Payment request failed. | Error sending payment command.");
            }

            log.debug("Payment request created successfully. Payment Status: {}", PaymentControl.REQUESTED);

            log.info("Payment request created successfully. Payment Status: {}", PaymentControl.REQUESTED);

            return new PaymentDTO(payment);
        }

    @Transactional
    @Override
    public void makePayment(PaymentCommandDTO paymentCommandDTO) {

        Payment payment = repository.findById(paymentCommandDTO.getPaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found id: " + paymentCommandDTO.getPaymentId()));

        User user = userRepository.findById(paymentCommandDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found id: " + paymentCommandDTO.getUserId()));

        CreditCard creditCard = creditCardRepository.findById(paymentCommandDTO.getCardId())
                .orElseThrow(() -> new ResourceNotFoundException("Credit Card not found id: " + paymentCommandDTO.getCardId()));

        paymentStripeServiceImpl.processStripePayment(payment, creditCard);

        repository.save(payment);

        if(payment.getPaymentControl().equals(PaymentControl.EFFECTED)){
            user.setPaymentStatus(PaymentStatus.PAYING);
            user.setLastPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
            user.setPaymentExpirationDate(LocalDateTime.now(ZoneId.of("UTC")).plusDays(30));
            if(user.getFirstPaymentDate() == null){
                user.setFirstPaymentDate(LocalDateTime.now(ZoneId.of("UTC")));
            }
        }else if(payment.getPaymentControl().equals(PaymentControl.REFUSED)){
            user.setPaymentStatus(PaymentStatus.DEBTOR);
        }

        userRepository.save(user);

        if(payment.getPaymentControl().equals(PaymentControl.EFFECTED)|| payment.getPaymentControl().equals(PaymentControl.REFUSED)){
            paymentEventPublisher.publishPaymentEvent(payment.coverToPaymentEventDTO());
        }else if(payment.getPaymentControl().equals(PaymentControl.ERROR)){

            System.out.println("Payment failed. Payment Status: {}" +  PaymentControl.ERROR);
        }

        log.debug("Payment processed successfully. Payment Status: {}", PaymentControl.EFFECTED);
        log.info("Payment processed successfully. Payment Status: {}", PaymentControl.EFFECTED);

    }

    @Transactional
    @Override
    public PaymentDTO update(UUID id, PaymentDTO dto) {
        return null;
    }

    @Transactional
    @Override
    public void deleteById(UUID id, UUID userId) {

        log.debug("DeleteById UUID id received: {} and userId received: {}", id, userId);

        Optional<Payment> obj = repository.findByIdAndUserId(id, userId);
        Payment entity = obj.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        repository.delete(entity);

        log.debug("Payment deleted successfully. Payment ID: {}", id);
        log.info("Payment deleted successfully. Payment ID: {}", id);

    }

    public void copyDtoToEntity(CreditCard creditCard, PaymentRequestDTO dto) {

        creditCard.setCardHolderFullName(dto.getCardHolderFullName());
        creditCard.setCardHolderCpf(dto.getCardHolderCpf());
        creditCard.setCreditCardNumber(dto.getCreditCardNumber());
        creditCard.setExpirationDate(dto.getExpirationDate());
        creditCard.setCvvCode(dto.getCvvCode());
    }

    public void copyDtoToEntity(Payment payment, PaymentDTO dto) {

        payment.setPaymentControl(dto.getPaymentControl());
        payment.setPaymentRequestDate(dto.getPaymentRequestDate());
        payment.setPaymentCompletionDate(dto.getPaymentCompletionDate());
        payment.setPaymentExpirationDate(dto.getPaymentExpirationDate());
        payment.setLastDigitsCreditCard(dto.getLastDigitsCreditCard());
        payment.setValuePaid(dto.getValuePaid());
        payment.setPaymentMessage(dto.getPaymentMessage());

        if(dto.isRecurrence()){
            payment.setRecurrence(true);
        }
    }

}
