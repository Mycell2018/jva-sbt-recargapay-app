package com.recargapay.app.domain.dto.user;

import com.recargapay.app.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDTO {

    @NotEmpty(message = "O nome completo não pode estar vazio")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String fullName;

    @NotEmpty(message = "O CPF não pode estar vazio")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres")
    @Schema(description = "CPF do usuário", example = "12345678901")
    private String cpf;

    @Email(message = "E-mail inválido")
    @NotEmpty(message = "O e-mail não pode estar vazio")
    @Schema(description = "E-mail do usuário", example = "joao@email.com")
    private String email;

    @NotEmpty(message = "O telefone não pode estar vazio")
    @Schema(description = "Telefone do usuário", example = "+55 11 91234-5678")
    private String phone;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Schema(description = "Data de nascimento", example = "1990-05-20")
    private LocalDate birthDate;

    @Override
    public String toString() {
        return StringUtils.objectToString(this);
    }
}
