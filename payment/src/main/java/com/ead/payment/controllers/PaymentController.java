package com.ead.payment.controllers;

import com.ead.payment.dtos.PaymentDTO;
import com.ead.payment.dtos.PaymentRequestDTO;
import com.ead.payment.services.impl.PaymentServiceImpl;
import com.ead.payment.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PaymentController {

    @Autowired
    private PaymentServiceImpl service;

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping(value = "/users/{userId}/payments")
    public ResponseEntity<PaymentDTO> requestPayment(@PathVariable(value = "userId") UUID userId,
                                                     @RequestBody @Valid PaymentRequestDTO dto) {

        PaymentDTO paymentDTO = service.requestPayment(userId, dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(paymentDTO);

    }

    @GetMapping(value = "/users/{userId}/payments")
    public ResponseEntity<Page<PaymentDTO>> findAllPaged(@PathVariable (value = "userId") UUID userId,
                                                         SpecificationTemplate.PaymentSpec spec,
                                                         @PageableDefault (page = 0, size = 12, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){

        Page<PaymentDTO> page;
        if(userId != null) {
            page = service.findAllPaged(SpecificationTemplate.paymentUserId(userId).and(spec), pageable);
        } else {
            page = service.findAllPaged(spec, pageable);
        }
        return ResponseEntity.ok().body(page);

    }

    @GetMapping(value = "/users/{userId}/payments/{paymentId}")
    public ResponseEntity<PaymentDTO> findById(@PathVariable(value = "userId") UUID userId,
                                               @PathVariable(value = "paymentId") UUID paymentId) {


        PaymentDTO dto = service.findById(paymentId, userId);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/users/{userId}/payments/{paymentId}")
    public ResponseEntity<Object> delete(@PathVariable(value = "userId") UUID userId,
                                         @PathVariable(value = "paymentId") UUID paymentId) {
        service.deleteById(paymentId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Payment deleted successfully. | Payment ID: " + paymentId);
    }
}
