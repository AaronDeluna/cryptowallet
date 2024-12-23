package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

@Data
public class CreateCryptoAccountDto {
    @Schema(description = "Логин пользователя")
    @JsonProperty("user_login")
    private String userLogin;
    @Schema(description = "Тип крипто-валюты")
    @JsonProperty("crypto_currency")
    private CryptoCurrency currency;
}
