package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("old_password")
    @Schema(description = "Старый пароль")
    @NonNull
    private String oldPassword;
    @JsonProperty("new_password")
    @Schema(description = "Новый пароль")
    @NonNull
    private String newPassword;
}
