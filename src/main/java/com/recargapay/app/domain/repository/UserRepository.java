package com.recargapay.app.domain.repository;

import com.recargapay.app.domain.model.User;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByCpf(String cpf);

    Optional<User> findByEmail(String email);
}
