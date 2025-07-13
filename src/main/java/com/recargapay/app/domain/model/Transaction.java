package com.recargapay.app.domain.model;

import com.recargapay.app.domain.enums.TransactionStatus;
import com.recargapay.app.domain.enums.TransactionType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "account_transactions", schema = "sc_recargapay")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    public static Object TransactionType;

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "related_account_id")
    private BankAccount relatedAccount;

    private UUID referenceId;

    private LocalDateTime createdAt;
}
