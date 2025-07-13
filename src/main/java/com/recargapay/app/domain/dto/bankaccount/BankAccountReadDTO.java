package com.recargapay.app.domain.dto.bankaccount;

import com.recargapay.app.domain.dto.user.UserReadDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountReadDTO {

    @Schema(description = "ID da conta bancária", example = "6f1dcb7e-7993-4a60-9e2a-8ef775fcf499")
    private UUID id;

    @Schema(description = "ID do usuário dono da conta", example = "c5d2d240-6c1e-4bc1-b94f-3a0a64c9c206")
    private UserReadDTO user;

    @Schema(description = "Número da conta", example = "12345678")
    private String accountNumber;

    @Schema(description = "Agência", example = "0001")
    private String agency;

    @Schema(description = "Tipo de conta", example = "DIGITAL")
    private String type;

    @Schema(description = "Status da conta", example = "ACTIVE")
    private String status;

    @Schema(description = "Data de criação", example = "2025-07-12T10:15:30")
    private LocalDateTime createdAt;
}
