package com.ead.payment.consumers;

import com.ead.payment.dtos.PaymentCommandDTO;
import com.ead.payment.services.impl.PaymentServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class PaymentConsumer {

    @Autowired
    private PaymentServiceImpl paymentServiceImpl;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.paymentCommandQueue.name}", durable = "true"),
            exchange = @Exchange(value = "${ead.broker.exchange.paymentCommandExchange}", type = ExchangeTypes.TOPIC, ignoreDeclarationExceptions = "true"),
            key = "${ead.broker.key.paymentCommandKey}")
    )
    public void listenPaymentCommand(@Payload PaymentCommandDTO dto) {

        log.debug("Payment command received. Payment ID: {}, User ID: {}, Card ID: {}", dto.getPaymentId(), dto.getUserId(), dto.getCardId());

        paymentServiceImpl.makePayment(dto);

        log.info("Payment command processed successfully. Payment ID: {}", dto.getPaymentId()
        );
    }
}
