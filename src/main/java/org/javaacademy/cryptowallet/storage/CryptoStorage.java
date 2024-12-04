package org.javaacademy.cryptowallet.storage;

import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class CryptoStorage {
    private static final String CRYPTO_ACCOUNT_ID_EXIST = "Ошибка: счет с таким id '%s' уже существует";
    private static final String CRYPTO_ACCOUNT_ID_NOTFOUND = "Ошибка: счета с id '%s' не найден";
    private final Map<UUID, CryptoAccount> cryptoStorage = new HashMap<>();

    public void save(CryptoAccount cryptoAccount) {
        if (cryptoStorage.containsKey(cryptoAccount.getUuid())) {
            throw new IllegalArgumentException(
                    CRYPTO_ACCOUNT_ID_EXIST.formatted(cryptoAccount.getUuid())
            );
        }
        cryptoStorage.put(cryptoAccount.getUuid(), cryptoAccount);
    }

    public CryptoAccount getCryptoAccountByUuid(UUID uuid) {
        return Optional.ofNullable(cryptoStorage.get(uuid))
                .orElseThrow(
                        () -> new IllegalArgumentException(CRYPTO_ACCOUNT_ID_NOTFOUND.formatted(uuid))
                );
    }

    public List<CryptoAccount> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoStorage.values().stream()
                .filter(cryptoAccount -> cryptoAccount.getUserLogin().equals(userLogin))
                .toList();
    }
}
