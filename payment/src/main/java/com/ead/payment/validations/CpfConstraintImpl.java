package com.ead.payment.validations;

import com.ead.payment.repositories.CreditCardRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CpfConstraintImpl implements ConstraintValidator<CpfConstraint, String> {

    @Autowired
    private CreditCardRepository repository;

    @Override
    public void initialize(CpfConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {

       boolean existsByCpf = repository.existsByCardHolderCpf(cpf);
        return !existsByCpf;
    }
}
