package com.recargapay.app.controller;

import com.recargapay.app.config.exception.RecordNotFoundException;
import com.recargapay.app.domain.dto.user.UserCreateDTO;
import com.recargapay.app.domain.dto.user.UserReadDTO;
import com.recargapay.app.domain.dto.user.UserUpdateDTO;
import com.recargapay.app.domain.mapper.UserMapper;
import com.recargapay.app.domain.model.User;
import com.recargapay.app.domain.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController implements UserSwagger {

    private final UserService service;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<UserReadDTO> create(UserCreateDTO dto) {
        log.info("UserCreateDTO: {}", dto);
        User entity = service.create(userMapper.createToEntity(dto));
        UserReadDTO response = userMapper.entityToRead(entity);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<List<UserReadDTO>> read() {
        List<User> entities = service.read();
        List<UserReadDTO> response = userMapper.listEntityToRead(entities);

        if (response.isEmpty()) {
            log.info("Nenhum usuário encontrado.");
            return ResponseEntity.noContent().build();
        }

        log.info("Total de usuários encontrados: {}", response.size());
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserReadDTO> readById(UUID id) {
        log.info("ID: {}", id);
        User entity = service.getUser(id).orElseThrow(() -> new RecordNotFoundException("Usuário não encontrado"));
        UserReadDTO result = userMapper.entityToRead(entity);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<UserReadDTO> readByCpf(String cpf) {
        log.info("CPF: {}", cpf);
        User entity = service.getByCpf(cpf).orElseThrow(() -> new RecordNotFoundException("Usuário não encontrado"));
        return ResponseEntity.ok(userMapper.entityToRead(entity));
    }

    @Override
    public ResponseEntity<UserReadDTO> update(UUID id, UserUpdateDTO dto) {
        log.info("ID: {}, DTO: {}", id, dto);
        User entity = service.update(id, userMapper.updateToEntity(dto));
        return ResponseEntity.ok(userMapper.entityToRead(entity));
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        log.info("Deletando usuário com ID: {}", id);

        if (!service.existsById(id)) {
            log.warn("Usuário com ID {} não encontrado", id);
            throw new RecordNotFoundException("Usuário não encontrado");
        }

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
