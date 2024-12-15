package org.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoAccountDto {
    private UUID uuid;
    private String userLogin;
    private CryptoCurrency currency;
    private BigDecimal currencyCount;
}
