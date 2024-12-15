package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

@Data
public class CreateCryptoAccountDto {
    @JsonProperty("user_login")
    private String userLogin;
    @JsonProperty("crypto_currency")
    private CryptoCurrency currency;
}
