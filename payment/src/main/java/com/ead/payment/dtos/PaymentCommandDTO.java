package com.ead.payment.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentCommandDTO {

    private UUID paymentId;
    private UUID userId;
    private UUID cardId;
}
