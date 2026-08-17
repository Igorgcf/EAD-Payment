package com.ead.payment.dtos;

import com.ead.payment.enums.PaymentStatus;
import com.ead.payment.models.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private UUID id;

    private String username;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private String actionType;

    private PaymentStatus paymentStatus;
    private LocalDateTime paymentExpirationDate;
    private LocalDateTime firstPaymentDate;
    private LocalDateTime lastPaymentDate;

    public UserDTO() {
    }

    public UserDTO(User entity){
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.email = entity.getEmail();
        this.fullName = entity.getFullName();
        this.userStatus = entity.getUserStatus();
        this.userType = entity.getUserType();
        this.phoneNumber = entity.getPhoneNumber();
        this.cpf = entity.getCpf();
        this.imageUrl = entity.getImageUrl();
        this.paymentStatus = entity.getPaymentStatus();
        this.paymentExpirationDate = entity.getPaymentExpirationDate();
        this.firstPaymentDate = entity.getFirstPaymentDate();
        this.lastPaymentDate = entity.getLastPaymentDate();
    }

    public User convertToUser() {
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setFullName(this.fullName);
        user.setUserStatus(this.userStatus);
        user.setUserType(this.userType);
        user.setPhoneNumber(this.phoneNumber);
        user.setCpf(this.cpf);
        user.setImageUrl(this.imageUrl);
        user.setPaymentStatus(this.paymentStatus);
        return user;
    }

}
