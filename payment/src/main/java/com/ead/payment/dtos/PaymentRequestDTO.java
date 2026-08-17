package com.ead.payment.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestDTO {

    @NotNull(message = "The field value paid can't null.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Min value accepted: R$ 0.1.")
    @Digits(integer = 5, fraction = 2)
    private BigDecimal valuePaid;

    @NotBlank(message = "The card holder full name field is mandatory and and blanks are not allowed.")
    @Size(min = 3, max = 70, message = "Minimum character value accepted: 03 and maximum: 70.")
    private String cardHolderFullName;

    @NotBlank(message = "The CPF field is mandatory and and blanks are not allowed.")
    @CPF(message = "Invalid CPF format | Required format: 000.000.000-00")
    private String cardHolderCpf;

    @NotBlank(message = "The credit card number field is mandatory and and blanks are not allowed.")
    @Size(min = 16, max = 20, message = "Minimum characters value accepted: 16 and maximum: 20")
    private String creditCardNumber;

    @NotBlank(message = "The expirations date field is mandatory and and blanks are not allowed.")
    @Size(min = 4, max = 10, message = "Minimum characters value accepted: 04 and maximum: 10")
    private String expirationDate;

    @NotBlank(message = "The Cvv Code field is mandatory and and blanks are not allowed.")
    @Size(min = 3, max = 3, message = "Minimum value accepted: 03 and maximum: 03")
    private String cvvCode;
}
