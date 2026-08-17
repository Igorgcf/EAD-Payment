package com.ead.payment.services.impl;

import com.ead.payment.dtos.UserDTO;
import com.ead.payment.models.User;
import com.ead.payment.repositories.UserRepository;
import com.ead.payment.services.UserService;
import com.ead.payment.services.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> page = repository.findAll(pageable);
        return page.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO findById(UUID id) {

        Optional<User> obj = repository.findById(id);

        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        return new UserDTO(entity);
    }

    @Transactional
    @Override
    public UserDTO insert(UserDTO dto) {

        log.debug("Insert UserDTO received {} ", dto.toString());

        User entity = dto.convertToUser();
        repository.save(entity);

        log.debug("Insert user saved: {} ", entity);
        log.info("User saved successfully Id: {} ", entity.getId());

        return new UserDTO(entity);
    }

    @Transactional
    @Override
    public UserDTO update(UUID id, UserDTO dto) {

        log.debug("UserDTO received: {} ", dto);

        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found: " + id));

        copyDtoToEntity(entity, dto);
        repository.save(entity);

        log.debug("Update User saved: {} ", entity);
        log.info("User updated successfully Id: {} ", entity.getId());

        return new UserDTO(entity);

    }

        @Override
        public void deleteById (UUID id){

            Optional<User> obj = repository.findById(id);
            if (obj.isEmpty()) {
                throw new ResourceNotFoundException("Id not found: " + id);
            }

            repository.deleteById(id);

        }

        void copyDtoToEntity (User entity, UserDTO dto){

            if (dto.getUsername() != null) {
                entity.setUsername(dto.getUsername());
            }

            if (dto.getEmail() != null) {
                entity.setEmail(dto.getEmail());
            }

            if (dto.getFullName() != null) {
                entity.setFullName(dto.getFullName());
            }

            if (dto.getUserStatus() != null) {
                entity.setUserStatus(dto.getUserStatus());
            }

            if (dto.getUserType() != null) {
                entity.setUserType(dto.getUserType());
            }

            if (dto.getPhoneNumber() != null) {
                entity.setPhoneNumber(dto.getPhoneNumber());
            }

            if (dto.getCpf() != null) {
                entity.setCpf(dto.getCpf());
            }

            if (dto.getImageUrl() != null) {
                entity.setImageUrl(dto.getImageUrl());
            }
        }
    }
