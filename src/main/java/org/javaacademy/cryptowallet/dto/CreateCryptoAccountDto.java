package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCryptoAccountDto {
    @Schema(description = "Логин пользователя")
    @JsonProperty("user_login")
    private String userLogin;
    @Schema(description = "Тип крипто-валюты")
    @JsonProperty("crypto_currency")
    private String currency;
}
