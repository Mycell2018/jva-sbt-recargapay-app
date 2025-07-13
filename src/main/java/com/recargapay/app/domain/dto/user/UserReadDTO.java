package com.recargapay.app.domain.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserReadDTO {

    @Schema(description = "Id do usuário", example = "267782a3-d46b-46e0-a5fe-e5d8061b72b4")
    private UUID id;

    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String fullName;

    @Schema(description = "CPF do usuário", example = "12345678901")
    private String cpf;

    @Schema(description = "E-mail do usuário", example = "joao@email.com")
    private String email;

    @Schema(description = "Telefone do usuário", example = "+55 11 91234-5678")
    private String phone;

    @Schema(description = "Data de nascimento", example = "1990-05-20")
    private LocalDate birthDate;
}
