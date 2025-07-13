package com.recargapay.app.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
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
@Table(name = "account_balances", schema = "sc_recargapay")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalance {
    @Id
    @JoinColumn(name = "account_id")
    private UUID accountId;

    private BigDecimal balance;

    private LocalDateTime updatedAt;
}
