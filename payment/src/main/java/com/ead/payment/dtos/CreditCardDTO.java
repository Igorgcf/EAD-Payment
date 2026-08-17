package com.ead.payment.dtos;

import com.ead.payment.models.CreditCard;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CreditCardDTO {

    private UUID id;
    private String cardHolderFullName;
    private String cardHolderCpf;
    private String creditCardNumber;
    private String expirationDate;
    private String cvvCode;

    private UserDTO userDTO;

    public CreditCardDTO(){
    }

   public CreditCardDTO(CreditCard entity){
        this.id = entity.getId();
        this.cardHolderFullName = entity.getCardHolderFullName();
        this.cardHolderCpf = entity.getCardHolderCpf();
        this.creditCardNumber = entity.getCreditCardNumber();
        this.expirationDate = entity.getExpirationDate();
        this.cvvCode = entity.getCvvCode();
        this.userDTO = new UserDTO(entity.getUser());
    }

}
