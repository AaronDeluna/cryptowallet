package org.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Schema(description = "Логин пользователя")
    private String userLogin;
    @Schema(description = "Почта")
    private String email;
    @Schema(description = "Пароль")
    private String password;
}
