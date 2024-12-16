package org.javaacademy.cryptowallet.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CryptoAccountStorageRepository {
    private static final String CRYPTO_ACCOUNT_ID_EXIST = "Ошибка: счет с таким id '%s' уже существует";
    private static final String CRYPTO_ACCOUNT_ID_NOTFOUND = "Ошибка: счета с id '%s' не найден";
    private final CryptoAccountStorage cryptoAccountStorage;

    public void save(CryptoAccount cryptoAccount) {
        if (getStorage().containsKey(cryptoAccount.getUuid())) {
            throw new IllegalArgumentException(
                    CRYPTO_ACCOUNT_ID_EXIST.formatted(cryptoAccount.getUuid())
            );
        }
        getStorage().put(cryptoAccount.getUuid(), cryptoAccount);
    }

    public CryptoAccount getCryptoAccountByUuid(UUID uuid) {
        return Optional.ofNullable(getStorage().get(uuid))
                .orElseThrow(
                        () -> new IllegalArgumentException(CRYPTO_ACCOUNT_ID_NOTFOUND.formatted(uuid))
                );
    }

    public List<CryptoAccount> getAllCryptoAccountByUserLogin(String userLogin) {
        return getStorage().values().stream()
                .filter(cryptoAccount -> cryptoAccount.getUserLogin().equals(userLogin))
                .toList();
    }

    private Map<UUID, CryptoAccount> getStorage() {
        return cryptoAccountStorage.getCryptoStorage();
    }
}
