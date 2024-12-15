package org.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CryptoAccountDto {
    private UUID uuid;
    private final String userLogin;
    private final CryptoCurrency currency;
    private BigDecimal currencyCount;
}
