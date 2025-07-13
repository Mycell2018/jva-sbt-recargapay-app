package com.recargapay.app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "users", schema = "sc_recargapay")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private UUID id;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    private String phone;

    private LocalDate birthDate;

    private LocalDateTime createdAt;
}
