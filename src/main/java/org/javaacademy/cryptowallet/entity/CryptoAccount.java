package org.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CryptoAccount {
    private UUID uuid;
    private String userLogin;
    private CryptoCurrency currency;
    private BigDecimal currencyCount;
}
