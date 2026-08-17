package com.ead.payment.repositories;

import com.ead.payment.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment>{

    // Me retorna o último pagamento do usuário ordenado pela data de solicitação de pagamento em ordem decrescente
    Optional<Payment> findTopByUserIdOrderByPaymentRequestDateDesc(UUID userId);

    Optional<Payment> findByIdAndUserId(UUID id, UUID userId);

}
