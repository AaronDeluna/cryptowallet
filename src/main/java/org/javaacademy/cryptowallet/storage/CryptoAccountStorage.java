package org.javaacademy.cryptowallet.storage;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> cryptoStorage = new HashMap<>();

    @PostConstruct
    public void init() {
        CryptoAccount cryptoAccount = new CryptoAccount(
                UUID.randomUUID(),
                "Anton",
                CryptoCurrency.BTC,
                BigDecimal.ZERO
        );
    }
}
