package com.recargapay.app.domain.service;

import com.recargapay.app.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    User create(User user);

    User update(UUID id, User newData);

    List<User> read();

    void delete(UUID id);

    boolean existsById(UUID id);

    Optional<User> getUser(UUID userId);

    Optional<User> getByCpf(String cpf);
}
