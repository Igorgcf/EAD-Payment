package com.ead.payment.models;

import com.ead.payment.dtos.PaymentEventDTO;
import com.ead.payment.enums.PaymentControl;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name= "tb_payment")
public class Payment implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentControl paymentControl;

    @Column(nullable = false)
    private LocalDateTime paymentRequestDate;

    @Column
    private LocalDateTime paymentCompletionDate;

    @Column(nullable = false)
    private LocalDateTime paymentExpirationDate;

    @Column(nullable = false, length = 4)
    private String lastDigitsCreditCard;

    @Column(nullable = false)
    private BigDecimal valuePaid;

    @Column(length = 150)
    private String paymentMessage;

    @Column
    private boolean recurrence;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private User user;

    public Payment() {
    }

    public PaymentEventDTO coverToPaymentEventDTO() {

        PaymentEventDTO dto = new PaymentEventDTO();
        dto.setId(this.getId());
        dto.setUserId(this.getUser().getId());
        dto.setPaymentControl(this.getPaymentControl().toString());
        dto.setPaymentRequestDate(this.getPaymentRequestDate());
        dto.setPaymentCompletionDate(this.getPaymentCompletionDate());
        dto.setPaymentExpirationDate(this.getPaymentExpirationDate());
        dto.setLastDigitsCreditCard(this.getLastDigitsCreditCard());
        dto.setValuePaid(this.getValuePaid());
        dto.setPaymentMessage(this.getPaymentMessage());
        dto.setRecurrence(this.isRecurrence());

        return dto;
    }
}
