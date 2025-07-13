package com.recargapay.app.domain.service.impl;

import com.recargapay.app.domain.enums.AccountStatus;
import com.recargapay.app.domain.enums.TransactionStatus;
import com.recargapay.app.domain.enums.TransactionType;
import com.recargapay.app.domain.model.AccountBalance;
import com.recargapay.app.domain.model.BankAccount;
import com.recargapay.app.domain.model.Transaction;
import com.recargapay.app.domain.model.User;
import com.recargapay.app.domain.repository.AccountBalanceRepository;
import com.recargapay.app.domain.repository.BankAccountRepository;
import com.recargapay.app.domain.repository.TransactionRepository;
import com.recargapay.app.domain.repository.UserRepository;
import com.recargapay.app.domain.service.BankAccountService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountBalanceRepository balanceRepository;

    @Override
    @Transactional
    public BankAccount createAccount(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        BankAccount account = BankAccount.builder()
                .id(UUID.randomUUID())
                .user(user)
                .accountNumber(generateAccountNumber())
                .agency("0001")
                .type("DIGITAL")
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        account = bankAccountRepository.save(account); // deve persistir primeiro

        balanceRepository.save(AccountBalance.builder()
                .accountId(account.getId())
                .balance(BigDecimal.ZERO)
                .updatedAt(LocalDateTime.now())
                .build());

        return account;
    }

    @Override
    public Optional<BankAccount> getAccount(UUID accountId) {
        return bankAccountRepository.findById(accountId);
    }

    @Override
    public List<BankAccount> getAccountsByUser(UUID userId) {
        return bankAccountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void deposit(UUID accountId, BigDecimal amount, String description) {
        BankAccount account =
                getAccount(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .description(description)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build());

        updateBalance(accountId, amount);
    }

    @Override
    @Transactional
    public void withdraw(UUID accountId, BigDecimal amount, String description) {
        BankAccount account =
                getAccount(accountId).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        BigDecimal current = getBalance(accountId);
        if (current.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");

        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(account)
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .description(description)
                .status(TransactionStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build());

        updateBalance(accountId, current.subtract(amount));
    }

    @Override
    @Transactional
    public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount, String description) {
        BankAccount from =
                getAccount(fromAccountId).orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        BankAccount to =
                getAccount(toAccountId).orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        BigDecimal current = getBalance(fromAccountId);
        if (current.compareTo(amount) < 0) throw new IllegalArgumentException("Insufficient funds");

        UUID referenceId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(from)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .description("TRANSFER TO " + to.getAccountNumber() + ": " + description)
                .status(TransactionStatus.COMPLETED)
                .relatedAccount(to)
                .referenceId(referenceId)
                .createdAt(now)
                .build());

        transactionRepository.save(Transaction.builder()
                .id(UUID.randomUUID())
                .account(to)
                .type(TransactionType.TRANSFER)
                .amount(amount)
                .description("TRANSFER FROM " + from.getAccountNumber() + ": " + description)
                .status(TransactionStatus.COMPLETED)
                .relatedAccount(from)
                .referenceId(referenceId)
                .createdAt(now)
                .build());

        updateBalance(fromAccountId, current.subtract(amount));
        updateBalance(toAccountId, getBalance(toAccountId).add(amount));
    }

    @Override
    public BigDecimal getBalance(UUID accountId) {
        return balanceRepository
                .findById(accountId)
                .map(AccountBalance::getBalance)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getHistoricalBalance(UUID accountId, LocalDateTime at) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .filter(tx ->
                        tx.getCreatedAt().isBefore(at) || tx.getCreatedAt().isEqual(at))
                .map(tx -> switch (tx.getType()) {
                    case DEPOSIT, TRANSFER -> tx.getAmount();
                    case WITHDRAWAL, FEE, REVERSAL -> tx.getAmount().negate();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Transaction> getStatement(UUID accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    private void updateBalance(UUID accountId, BigDecimal newBalance) {
        AccountBalance balance = balanceRepository
                .findById(accountId)
                .orElse(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.ZERO)
                        .build());

        balance.setBalance(newBalance);
        balance.setUpdatedAt(LocalDateTime.now());
        balanceRepository.save(balance);
    }

    private String generateAccountNumber() {
        return String.valueOf((int) (Math.random() * 90000000) + 10000000);
    }
}
