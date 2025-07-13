package com.recargapay.app.domain.repository;

import com.recargapay.app.domain.model.Transaction;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
}
