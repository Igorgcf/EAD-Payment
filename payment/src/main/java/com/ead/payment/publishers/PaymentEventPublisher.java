package com.ead.payment.publishers;

import com.ead.payment.dtos.PaymentEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventPublisher {

    @Value(value = "${ead.broker.exchange.paymentEventExchange}")
    private String paymentEventExchange;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publishPaymentEvent(PaymentEventDTO paymentEventDTO) {

        rabbitTemplate.convertAndSend(paymentEventExchange, "", paymentEventDTO);

    }
}
