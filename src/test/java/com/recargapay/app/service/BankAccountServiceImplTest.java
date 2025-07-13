package com.recargapay.app.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.recargapay.app.domain.enums.AccountStatus;
import com.recargapay.app.domain.enums.TransactionType;
import com.recargapay.app.domain.model.*;
import com.recargapay.app.domain.repository.*;
import com.recargapay.app.domain.service.impl.BankAccountServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountBalanceRepository balanceRepository;

    @InjectMocks
    private BankAccountServiceImpl service;

    private final UUID userId = UUID.randomUUID();
    private final UUID accountId = UUID.randomUUID();
    private final User user = User.builder().id(userId).build();
    private final BankAccount account = BankAccount.builder()
            .id(accountId)
            .user(user)
            .accountNumber("12345678")
            .agency("0001")
            .status(AccountStatus.ACTIVE)
            .createdAt(LocalDateTime.now())
            .build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bankAccountRepository.save(any())).thenReturn(account);

        BankAccount result = service.createAccount(userId);
        assertThat(result).isNotNull();
        verify(balanceRepository).save(any());
    }

    @Test
    void deposit() {
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(balanceRepository.findById(accountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.ZERO)
                        .build()));

        service.deposit(accountId, BigDecimal.TEN, "Depósito");

        verify(transactionRepository).save(any());
        verify(balanceRepository).save(any());
    }

    @Test
    void withdraw_withSufficientFunds() {
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(balanceRepository.findById(accountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.TEN)
                        .build()));

        service.withdraw(accountId, BigDecimal.ONE, "Saque");

        verify(transactionRepository).save(any());
        verify(balanceRepository).save(any());
    }

    @Test
    void withdraw_withInsufficientFunds() {
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(balanceRepository.findById(accountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.ZERO)
                        .build()));

        assertThrows(IllegalArgumentException.class, () -> service.withdraw(accountId, BigDecimal.ONE, "Saque"));
    }

    @Test
    void transfer() {
        UUID toAccountId = UUID.randomUUID();
        BankAccount toAccount = BankAccount.builder()
                .id(toAccountId)
                .user(user)
                .accountNumber("87654321")
                .agency("0001")
                .status(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(bankAccountRepository.findById(toAccountId)).thenReturn(Optional.of(toAccount));
        when(balanceRepository.findById(accountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.TEN)
                        .build()));
        when(balanceRepository.findById(toAccountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(toAccountId)
                        .balance(BigDecimal.ZERO)
                        .build()));

        service.transfer(accountId, toAccountId, BigDecimal.ONE, "Pagamento");

        verify(transactionRepository, times(2)).save(any());
        verify(balanceRepository, times(2)).save(any());
    }

    @Test
    void getBalance() {
        when(balanceRepository.findById(accountId))
                .thenReturn(Optional.of(AccountBalance.builder()
                        .accountId(accountId)
                        .balance(BigDecimal.valueOf(55))
                        .build()));
        BigDecimal result = service.getBalance(accountId);
        assertThat(result).isEqualTo(BigDecimal.valueOf(55));
    }

    @Test
    void getHistoricalBalance() {
        Transaction tx1 = Transaction.builder()
                .createdAt(LocalDateTime.now().minusDays(1))
                .amount(BigDecimal.TEN)
                .type(TransactionType.DEPOSIT)
                .build();
        Transaction tx2 = Transaction.builder()
                .createdAt(LocalDateTime.now().minusDays(1))
                .amount(BigDecimal.ONE)
                .type(TransactionType.WITHDRAWAL)
                .build();
        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId))
                .thenReturn(List.of(tx1, tx2));

        BigDecimal result = service.getHistoricalBalance(accountId, LocalDateTime.now());
        assertThat(result).isEqualTo(BigDecimal.valueOf(9));
    }

    @Test
    void getStatement() {
        List<Transaction> list = List.of(Transaction.builder().build());
        when(transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId))
                .thenReturn(list);
        List<Transaction> result = service.getStatement(accountId);
        assertThat(result).hasSize(1);
    }
}
