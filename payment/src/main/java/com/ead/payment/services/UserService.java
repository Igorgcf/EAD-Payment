package com.ead.payment.services;

import com.ead.payment.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    Page<UserDTO> findAllPaged(Pageable pageable);

    UserDTO findById(UUID id);

    UserDTO insert (UserDTO dto);

    UserDTO update(UUID id, UserDTO dto);

    void deleteById(UUID id);
}
