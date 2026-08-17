package com.ead.payment.services.impl;

import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.CreditCard;
import com.ead.payment.models.Payment;
import com.ead.payment.services.PaymentStripeService;
import com.ead.payment.services.exceptions.BadRequestException;
import com.stripe.Stripe;
import com.stripe.exception.CardException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class PaymentStripeServiceImpl implements PaymentStripeService {

    @Value(value = "${ead.stripe.secretKey}")
    private String secretKey;

    @Override
    public void processStripePayment(Payment payment, CreditCard creditCard) {

        Stripe.apiKey = secretKey;

        String paymentIntentId = null;

        try {

            log.info("Stripe Pass 01 - Creating PaymentIntent");

            List<Object> paymentMethodTypes = new ArrayList<>();
            paymentMethodTypes.add("card");

            Map<String, Object> paramsPaymentIntent = new HashMap<>();
            paramsPaymentIntent.put("amount", payment.getValuePaid().multiply(new BigDecimal("100")).longValue());
            paramsPaymentIntent.put("currency", "brl");
            paramsPaymentIntent.put("payment_method_types", paymentMethodTypes);
            PaymentIntent paymentIntent = PaymentIntent.create(paramsPaymentIntent);

            paymentIntentId = paymentIntent.getId();

            log.info("Stripe Pass 01 - PaymentIntent created: {}", paymentIntent.getId());


            log.info("Stripe Pass 02 - Retrieving test PaymentMethod");

            PaymentMethod paymentMethod = PaymentMethod.retrieve("pm_card_visa");

            log.info("Stripe Pass 02 - PaymentMethod retrieved: {}", paymentMethod.getId());


            log.info("Stripe Pass 03 - Confirming PaymentIntent");

            Map<String, Object> paramsPaymentIntentConfirm = new HashMap<>();
            paramsPaymentIntentConfirm.put("payment_method", paymentMethod.getId());
            PaymentIntent confirmPaymentIntent = paymentIntent.confirm(paramsPaymentIntentConfirm);

            if (confirmPaymentIntent.getStatus().equals("succeeded")) {
                payment.setPaymentControl(PaymentControl.EFFECTED);
                payment.setPaymentMessage("Payment effected successfully - payment intent Id: " + paymentIntentId);
                payment.setPaymentCompletionDate(LocalDateTime.now(ZoneId.of("UTC")));
            } else {
                payment.setPaymentControl(PaymentControl.ERROR);
                payment.setPaymentMessage("Payment error v1 - payment intent Id: " + paymentIntentId);
            }

            log.info("Stripe Pass 03 - PaymentIntent confirmed. Status: {}", confirmPaymentIntent.getStatus());

            } catch (CardException cardException) {

            System.out.println("A payment error occurred: {}");

            try {
                payment.setPaymentControl(PaymentControl.REFUSED);
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
                payment.setPaymentMessage("Payment refused v1 - payment intent Id: " + paymentIntentId +
                        ", cause: " + paymentIntent.getLastPaymentError().getCode() +
                        ", message: " + paymentIntent.getLastPaymentError().getMessage());

            }catch (Exception exception) {
                payment.setPaymentMessage("payment refused v2 - payment intent Id: " + paymentIntentId);
                System.out.println("Another problem occurred, maybe unrelated to Stripe.");
                }

            } catch (Exception exception) {

                payment.setPaymentControl(PaymentControl.ERROR);
                payment.setPaymentMessage("Payment error v2 - payment intent Id: " + paymentIntentId);

                System.out.println("Another problem occurred, maybe unrelated to Stripe.");

                log.error("Stripe payment processing failed", exception);

                throw new BadRequestException("Stripe payment processing failed");
            }

        }
}