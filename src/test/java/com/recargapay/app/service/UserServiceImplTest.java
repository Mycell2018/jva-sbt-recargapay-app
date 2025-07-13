package com.recargapay.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.recargapay.app.config.exception.RecordNotFoundException;
import com.recargapay.app.domain.model.User;
import com.recargapay.app.domain.repository.UserRepository;
import com.recargapay.app.domain.service.impl.UserServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final UUID userId = UUID.randomUUID();
    private final User user = User.builder()
            .id(userId)
            .cpf("12345678900")
            .fullName("João da Silva")
            .phone("11999999999")
            .birthDate(LocalDateTime.now().minusYears(30).toLocalDate())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser() {
        when(userRepository.save(any())).thenReturn(user);

        User result = userService.create(user);
        assertThat(result).isNotNull();
        verify(userRepository).save(any());
    }

    @Test
    void updateUser_whenExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User update = User.builder()
                .fullName("Novo Nome")
                .phone("11988888888")
                .birthDate(user.getBirthDate())
                .build();
        User result = userService.update(userId, update);

        assertThat(result.getFullName()).isEqualTo("Novo Nome");
        verify(userRepository).save(any());
    }

    @Test
    void updateUser_whenNotExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(RecordNotFoundException.class, () -> userService.update(userId, user));
    }

    @Test
    void readUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.read();
        assertThat(result).hasSize(1);
    }

    @Test
    void deleteUser_whenExists() {
        when(userRepository.existsById(userId)).thenReturn(true);
        userService.delete(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUser_whenNotExists() {
        when(userRepository.existsById(userId)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> userService.delete(userId));
    }

    @Test
    void existsById() {
        when(userRepository.existsById(userId)).thenReturn(true);
        boolean exists = userService.existsById(userId);
        assertThat(exists).isTrue();
    }

    @Test
    void getUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUser(userId);
        assertThat(result).isPresent();
    }

    @Test
    void getByCpf() {
        when(userRepository.findByCpf("12345678900")).thenReturn(Optional.of(user));
        Optional<User> result = userService.getByCpf("12345678900");
        assertThat(result).isPresent();
    }
}
