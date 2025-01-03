package org.javaacademy.cryptowallet.storage;

import lombok.Getter;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Getter
public class CryptoAccountStorage {
    private final Map<UUID, CryptoAccount> cryptoStorage = new HashMap<>();

}
