package com.recargapay.app.domain.service.impl;

import com.recargapay.app.config.exception.RecordNotFoundException;
import com.recargapay.app.domain.model.User;
import com.recargapay.app.domain.repository.UserRepository;
import com.recargapay.app.domain.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public User update(UUID id, User newData) {
        return userRepository
                .findById(id)
                .map(existing -> {
                    existing.setFullName(newData.getFullName());
                    existing.setPhone(newData.getPhone());
                    existing.setBirthDate(newData.getBirthDate());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new RecordNotFoundException("Usuário não encontrado"));
    }

    @Override
    public List<User> read() {
        return userRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RecordNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> getUser(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }
}
