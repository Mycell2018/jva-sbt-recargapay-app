package com.recargapay.app.controller;

import com.recargapay.app.domain.dto.bankaccount.BankAccountReadDTO;
import com.recargapay.app.domain.dto.bankaccount.TransactionReadDTO;
import com.recargapay.app.domain.mapper.BankAccountMapper;
import com.recargapay.app.domain.mapper.TransactionMapper;
import com.recargapay.app.domain.model.BankAccount;
import com.recargapay.app.domain.model.Transaction;
import com.recargapay.app.domain.service.BankAccountService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BankAccountController implements BankAccountSwagger {

    private final BankAccountService service;
    private final BankAccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    @Override
    public ResponseEntity<BankAccountReadDTO> create(UUID userId) {
        BankAccount account = service.createAccount(userId);
        return ResponseEntity.status(201).body(accountMapper.entityToRead(account));
    }

    @Override
    public ResponseEntity<BankAccountReadDTO> read(UUID accountId) {
        return service.getAccount(accountId)
                .map(accountMapper::entityToRead)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<BankAccountReadDTO>> list(UUID userId) {
        List<BankAccount> accounts = service.getAccountsByUser(userId);
        return ResponseEntity.ok(accountMapper.listEntityToRead(accounts));
    }

    @Override
    public ResponseEntity<Void> deposit(UUID accountId, BigDecimal amount, String description) {
        service.deposit(accountId, amount, description);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> withdraw(UUID accountId, BigDecimal amount, String description) {
        service.withdraw(accountId, amount, description);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String description) {
        service.transfer(fromAccountId, toAccountId, amount, description);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<BigDecimal> getBalance(UUID accountId) {
        BigDecimal balance = service.getBalance(accountId);
        return ResponseEntity.ok(balance);
    }

    @Override
    public ResponseEntity<BigDecimal> getHistoricalBalance(UUID accountId, LocalDateTime at) {
        BigDecimal balance = service.getHistoricalBalance(accountId, at);
        return ResponseEntity.ok(balance);
    }

    @Override
    public ResponseEntity<List<TransactionReadDTO>> getStatement(UUID accountId) {
        List<Transaction> transactions = service.getStatement(accountId);
        return ResponseEntity.ok(transactionMapper.listEntityToRead(transactions));
    }
}
