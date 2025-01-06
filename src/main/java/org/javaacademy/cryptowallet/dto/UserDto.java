package org.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    @Schema(description = "Логин пользователя")
    @NonNull
    private String userLogin;
    @Schema(description = "Почта")
    @NonNull
    private String email;
    @Schema(description = "Пароль")
    @NonNull
    private String password;
}
