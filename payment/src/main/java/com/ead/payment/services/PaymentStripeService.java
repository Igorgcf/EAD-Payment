package com.ead.payment.services;

import com.ead.payment.dtos.PaymentDTO;
import com.ead.payment.models.CreditCard;
import com.ead.payment.models.Payment;

public interface PaymentStripeService {

    void processStripePayment(Payment payment, CreditCard creditCard);
}
