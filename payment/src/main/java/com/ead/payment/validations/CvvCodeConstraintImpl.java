package com.ead.payment.validations;

import com.ead.payment.repositories.CreditCardRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class CvvCodeConstraintImpl implements ConstraintValidator<CvvCodeConstraint, String> {

    @Autowired
    private CreditCardRepository repository;

    @Override
    public void initialize(CvvCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cvvCode, ConstraintValidatorContext constraintValidatorContext) {

        boolean existsByCvvCode = repository.existsByCvvCode(cvvCode);
        return !existsByCvvCode;
    }
}
