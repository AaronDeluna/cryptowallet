package org.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CryptoAccount {
    private UUID uuid = UUID.randomUUID();
    private final String userLogin;
    private final CryptoCurrency currency;
    private BigDecimal currencyCount = BigDecimal.ZERO;

}
