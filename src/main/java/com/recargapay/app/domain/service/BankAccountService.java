package com.recargapay.app.domain.service;

import com.recargapay.app.domain.model.BankAccount;
import com.recargapay.app.domain.model.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public interface BankAccountService {
    BankAccount createAccount(UUID userId);

    Optional<BankAccount> getAccount(UUID accountId);

    List<BankAccount> getAccountsByUser(UUID userId);

    void deposit(UUID accountId, BigDecimal amount, String description);

    void withdraw(UUID accountId, BigDecimal amount, String description);

    void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String description);

    BigDecimal getBalance(UUID accountId);

    BigDecimal getHistoricalBalance(UUID accountId, LocalDateTime at);

    List<Transaction> getStatement(UUID accountId);
}
