package com.ead.payment.services;

import com.ead.payment.dtos.PaymentCommandDTO;
import com.ead.payment.dtos.PaymentDTO;
import com.ead.payment.dtos.PaymentRequestDTO;
import com.ead.payment.models.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public interface PaymentService {

    Page<PaymentDTO> findAllPaged(Specification<Payment> spec, Pageable pageable);

    PaymentDTO findById(UUID userId, UUID paymentId);

    PaymentDTO requestPayment(UUID id, PaymentRequestDTO dto);

    void makePayment(PaymentCommandDTO paymentCommandDTO);

    PaymentDTO update(UUID id, PaymentDTO dto);

    void deleteById(UUID userId, UUID paymentId);

}
