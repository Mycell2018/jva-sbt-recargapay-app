package com.recargapay.app.domain.dto.user;

import com.recargapay.app.utils.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @NotEmpty(message = "O id não pode estar vazio")
    @Schema(description = "Id do usuário", example = "267782a3-d46b-46e0-a5fe-e5d8061b72b4")
    private String id;

    @NotEmpty(message = "O nome completo não pode estar vazio")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres")
    @Schema(description = "Nome completo do usuário", example = "João da Silva")
    private String fullName;

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
