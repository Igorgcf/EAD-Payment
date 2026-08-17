package com.ead.payment.controllers;

import com.ead.payment.dtos.UserDTO;
import com.ead.payment.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3700)
public class UserController {

    @Autowired
    private UserServiceImpl service;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable) {

        Page<UserDTO> page = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(page);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable(value = "id") UUID id) {
        UserDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }
}
