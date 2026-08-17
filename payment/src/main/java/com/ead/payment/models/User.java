package com.ead.payment.models;

import com.ead.payment.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_user")
public class User implements Serializable {
    private static final long SerialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 150)
    private String fullName;

    @Column(nullable = false)
    private String userStatus;

    @Column(nullable = false)
    private String userType;

    @Column(length = 50)
    private String phoneNumber;

    @Column(length = 50)
    private String cpf;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime paymentExpirationDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime firstPaymentDate;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastPaymentDate;

    @Column
    private String imageUrl;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user")
    @Fetch(FetchMode.SUBSELECT)
    private List<Payment> payments = new ArrayList<>();



}
