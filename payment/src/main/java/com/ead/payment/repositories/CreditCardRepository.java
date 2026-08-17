package com.ead.payment.repositories;

import com.ead.payment.models.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID>, JpaSpecificationExecutor<CreditCard> {

    Optional<CreditCard> findByUserId(UUID userId);

    boolean existsByCardHolderCpf(String cardHolderCpf);

    boolean existsByCvvCode (String cvvCode);

    Optional<CreditCard> findByCreditCardNumber(String creditCardNumber);
}
