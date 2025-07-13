package com.recargapay.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class BankAccountControllerTest {

    @Mock
    private BankAccountService service;

    @Mock
    private BankAccountMapper accountMapper;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private BankAccountController controller;

    private final UUID userId = UUID.randomUUID();
    private final UUID accountId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        BankAccount entity = new BankAccount();
        BankAccountReadDTO dto = new BankAccountReadDTO();
        when(service.createAccount(userId)).thenReturn(entity);
        when(accountMapper.entityToRead(entity)).thenReturn(dto);

        ResponseEntity<BankAccountReadDTO> response = controller.create(userId);
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void read() {
        BankAccount entity = new BankAccount();
        BankAccountReadDTO dto = new BankAccountReadDTO();
        when(service.getAccount(accountId)).thenReturn(Optional.of(entity));
        when(accountMapper.entityToRead(entity)).thenReturn(dto);

        ResponseEntity<BankAccountReadDTO> response = controller.read(accountId);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(dto);
    }

    @Test
    void list() {
        List<BankAccount> entities = List.of(new BankAccount());
        List<BankAccountReadDTO> dtos = List.of(new BankAccountReadDTO());
        when(service.getAccountsByUser(userId)).thenReturn(entities);
        when(accountMapper.listEntityToRead(entities)).thenReturn(dtos);

        ResponseEntity<List<BankAccountReadDTO>> response = controller.list(userId);
        assertThat(response.getBody()).isEqualTo(dtos);
    }

    @Test
    void deposit() {
        ResponseEntity<Void> response = controller.deposit(accountId, BigDecimal.TEN, "desc");
        verify(service).deposit(accountId, BigDecimal.TEN, "desc");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void withdraw() {
        ResponseEntity<Void> response = controller.withdraw(accountId, BigDecimal.ONE, "withdraw");
        verify(service).withdraw(accountId, BigDecimal.ONE, "withdraw");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void transfer() {
        UUID toAccountId = UUID.randomUUID();
        ResponseEntity<Void> response = controller.transfer(accountId, toAccountId, BigDecimal.ONE, "transf");
        verify(service).transfer(accountId, toAccountId, BigDecimal.ONE, "transf");
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    void getBalance() {
        when(service.getBalance(accountId)).thenReturn(BigDecimal.valueOf(100));
        ResponseEntity<BigDecimal> response = controller.getBalance(accountId);
        assertThat(response.getBody()).isEqualTo(BigDecimal.valueOf(100));
    }

    @Test
    void getHistoricalBalance() {
        LocalDateTime dateTime = LocalDateTime.now();
        when(service.getHistoricalBalance(accountId, dateTime)).thenReturn(BigDecimal.valueOf(50));
        ResponseEntity<BigDecimal> response = controller.getHistoricalBalance(accountId, dateTime);
        assertThat(response.getBody()).isEqualTo(BigDecimal.valueOf(50));
    }

    @Test
    void getStatement() {
        List<Transaction> entities = List.of(new Transaction());
        List<TransactionReadDTO> dtos = List.of(new TransactionReadDTO());
        when(service.getStatement(accountId)).thenReturn(entities);
        when(transactionMapper.listEntityToRead(entities)).thenReturn(dtos);

        ResponseEntity<List<TransactionReadDTO>> response = controller.getStatement(accountId);
        assertThat(response.getBody()).isEqualTo(dtos);
    }
}
