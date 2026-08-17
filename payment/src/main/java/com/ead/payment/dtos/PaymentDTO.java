package com.ead.payment.dtos;

import com.ead.payment.enums.PaymentControl;
import com.ead.payment.models.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PaymentDTO {

    private UUID id;

    private PaymentControl paymentControl;
    private LocalDateTime paymentRequestDate;
    private LocalDateTime paymentCompletionDate;
    private LocalDateTime paymentExpirationDate;
    private String lastDigitsCreditCard;
    private BigDecimal valuePaid;
    private String paymentMessage;

    private boolean recurrence;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserDTO userDTO;

    public PaymentDTO(){
    }

    public PaymentDTO(Payment entity){

        this.id = entity.getId();
        this.paymentControl = entity.getPaymentControl();
        this.paymentRequestDate = entity.getPaymentRequestDate();
        this.paymentCompletionDate = entity.getPaymentCompletionDate();
        this.paymentExpirationDate = entity.getPaymentExpirationDate();
        this.lastDigitsCreditCard = entity.getLastDigitsCreditCard();
        this.valuePaid = entity.getValuePaid();
        this.paymentMessage = entity.getPaymentMessage();
        this.recurrence = entity.isRecurrence();
        this.userDTO = new UserDTO(entity.getUser());
    }
}
