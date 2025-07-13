package com.recargapay.app.domain.repository;

import com.recargapay.app.domain.model.BankAccount;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    Optional<BankAccount> findByAccountNumberAndAgency(String accountNumber, String agency);

    List<BankAccount> findByUserId(UUID userId);
}
