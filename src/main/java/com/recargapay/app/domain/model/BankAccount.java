package com.recargapay.app.domain.model;

import com.recargapay.app.domain.enums.AccountStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "bank_accounts", schema = "sc_recargapay")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String accountNumber;

    private String agency;

    private String type;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private LocalDateTime createdAt;
}
