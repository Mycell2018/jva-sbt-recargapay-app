package com.recargapay.app.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.recargapay.app.config.exception.RecordNotFoundException;
import com.recargapay.app.domain.dto.user.UserCreateDTO;
import com.recargapay.app.domain.dto.user.UserReadDTO;
import com.recargapay.app.domain.dto.user.UserUpdateDTO;
import com.recargapay.app.domain.mapper.UserMapper;
import com.recargapay.app.domain.model.User;
import com.recargapay.app.domain.service.UserService;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        UserCreateDTO dto = new UserCreateDTO();
        User user = new User();
        UserReadDTO responseDTO = new UserReadDTO();

        when(userMapper.createToEntity(dto)).thenReturn(user);
        when(userService.create(user)).thenReturn(user);
        when(userMapper.entityToRead(user)).thenReturn(responseDTO);

        ResponseEntity<UserReadDTO> response = userController.create(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void read_withUsers() {
        List<User> users = List.of(new User());
        List<UserReadDTO> dtos = List.of(new UserReadDTO());

        when(userService.read()).thenReturn(users);
        when(userMapper.listEntityToRead(users)).thenReturn(dtos);

        ResponseEntity<List<UserReadDTO>> response = userController.read();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dtos, response.getBody());
    }

    @Test
    void read_noUsers() {
        when(userService.read()).thenReturn(Collections.emptyList());
        when(userMapper.listEntityToRead(Collections.emptyList())).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserReadDTO>> response = userController.read();

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void readById_found() {
        User user = new User();
        UserReadDTO dto = new UserReadDTO();

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        when(userMapper.entityToRead(user)).thenReturn(dto);

        ResponseEntity<UserReadDTO> response = userController.readById(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void readById_notFound() {
        when(userService.getUser(userId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> userController.readById(userId));
    }

    @Test
    void readByCpf_found() {
        String cpf = "12345678900";
        User user = new User();
        UserReadDTO dto = new UserReadDTO();

        when(userService.getByCpf(cpf)).thenReturn(Optional.of(user));
        when(userMapper.entityToRead(user)).thenReturn(dto);

        ResponseEntity<UserReadDTO> response = userController.readByCpf(cpf);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void readByCpf_notFound() {
        String cpf = "12345678900";
        when(userService.getByCpf(cpf)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> userController.readByCpf(cpf));
    }

    @Test
    void update() {
        UserUpdateDTO dto = new UserUpdateDTO();
        User user = new User();
        UserReadDTO responseDTO = new UserReadDTO();

        when(userMapper.updateToEntity(dto)).thenReturn(user);
        when(userService.update(userId, user)).thenReturn(user);
        when(userMapper.entityToRead(user)).thenReturn(responseDTO);

        ResponseEntity<UserReadDTO> response = userController.update(userId, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void delete_success() {
        when(userService.existsById(userId)).thenReturn(true);
        doNothing().when(userService).delete(userId);

        ResponseEntity<Void> response = userController.delete(userId);

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void delete_notFound() {
        when(userService.existsById(userId)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> userController.delete(userId));
    }
}
