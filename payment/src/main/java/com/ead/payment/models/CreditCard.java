package com.ead.payment.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name ="tb_credit_card")
public class CreditCard implements Serializable {

    private static final long SerialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String cardHolderFullName;

    @Column(unique = true, nullable = false, length = 20)
    private String cardHolderCpf;

    @Column(nullable = false, length = 20)
    private String creditCardNumber;

    @Column(nullable = false, length = 10)
    private String expirationDate;

    @Column(unique = true, nullable = false, length = 3)
    private String cvvCode;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;
}
