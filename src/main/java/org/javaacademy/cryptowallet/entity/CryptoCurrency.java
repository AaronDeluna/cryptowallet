package org.javaacademy.cryptowallet.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CryptoCurrency {
    BTC("bitcoin"),
    ETH("ethereum"),
    SOL("solana"),
    ;

    private final String desc;
}
