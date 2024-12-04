package org.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CryptoAccount {
    private UUID uuid;
    private String userLogin;
    private CryptoCurrency currency;
    private BigDecimal currencyCount = BigDecimal.ZERO;
}
