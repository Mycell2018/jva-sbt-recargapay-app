package com.recargapay.app.controller;

import com.recargapay.app.config.exception.dto.ErrorResponse;
import com.recargapay.app.domain.dto.bankaccount.BankAccountReadDTO;
import com.recargapay.app.domain.dto.bankaccount.TransactionReadDTO;
import com.recargapay.app.domain.model.BankAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/accounts")
@Tag(name = "BankAccount", description = "API for managing bank accounts")
public interface BankAccountSwagger {

    @Operation(summary = "Create account", description = "Create a bank account for a user")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Created",
                content =
                        @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = BankAccount.class))),
        @ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/user/{userId}")
    ResponseEntity<BankAccountReadDTO> create(@PathVariable UUID userId);

    @Operation(summary = "Get account", description = "Get an account by ID")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = @Content(schema = @Schema(implementation = BankAccount.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{accountId}")
    ResponseEntity<BankAccountReadDTO> read(@PathVariable UUID accountId);

    @Operation(summary = "List user accounts", description = "Get all accounts of a user")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Success",
                content = @Content(schema = @Schema(implementation = BankAccount.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Not Found",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<List<BankAccountReadDTO>> list(@PathVariable UUID userId);

    @Operation(summary = "Deposit", description = "Make a deposit to an account")
    @PostMapping("/{accountId}/deposit")
    ResponseEntity<Void> deposit(
            @PathVariable UUID accountId, @RequestParam BigDecimal amount, @RequestParam String description);

    @Operation(summary = "Withdraw", description = "Withdraw from an account")
    @PostMapping("/{accountId}/withdraw")
    ResponseEntity<Void> withdraw(
            @PathVariable UUID accountId, @RequestParam BigDecimal amount, @RequestParam String description);

    @Operation(summary = "Transfer", description = "Transfer between accounts")
    @PostMapping("/{fromAccountId}/transfer/{toAccountId}")
    ResponseEntity<Void> transfer(
            @PathVariable UUID fromAccountId,
            @PathVariable UUID toAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam String description);

    @Operation(summary = "Get current balance", description = "Get current balance of an account")
    @GetMapping("/{accountId}/balance")
    ResponseEntity<BigDecimal> getBalance(@PathVariable UUID accountId);

    @Operation(summary = "Get historical balance", description = "Get balance of an account at a specific date")
    @GetMapping("/{accountId}/balance-at")
    ResponseEntity<BigDecimal> getHistoricalBalance(@PathVariable UUID accountId, @RequestParam LocalDateTime at);

    @Operation(summary = "Get account statement", description = "Get list of transactions")
    @GetMapping("/{accountId}/statement")
    ResponseEntity<List<TransactionReadDTO>> getStatement(@PathVariable UUID accountId);
}
