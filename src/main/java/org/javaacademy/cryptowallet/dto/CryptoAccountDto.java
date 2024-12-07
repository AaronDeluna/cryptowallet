package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoAccountDto {
    private UUID uuid;
    @JsonProperty("user_login")
    private final String userLogin;
    @JsonProperty("crypto_currency")
    private final CryptoCurrency currency;
    @JsonProperty("currency_count")
    private BigDecimal currencyCount;
}
