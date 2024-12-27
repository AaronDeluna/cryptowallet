package org.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetUserPasswordDto {
    @Schema(description = "Логин пользователя")
    @NonNull
    private String login;
    @Schema(description = "Старый пароль")
    @NonNull
    private String oldPassword;
    @Schema(description = "Новый пароль")
    @NonNull
    private String newPassword;
}
