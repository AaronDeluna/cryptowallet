package org.javaacademy.cryptowallet.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Component
@RequiredArgsConstructor
public class CryptoAccountRepository {
    private static final String CRYPTO_ACCOUNT_ID_EXIST = "Ошибка: счет с таким id '%s' уже существует";
    private static final String CRYPTO_ACCOUNT_ID_NOTFOUND = "Счет с id '%s' не найден";
    private final CryptoAccountStorage cryptoAccountStorage;

    public void save(CryptoAccount cryptoAccount) {
        cryptoAccount.setUuid(UUID.randomUUID());
        cryptoAccount.setCurrencyCount(ZERO);
        if (getStorage().containsKey(cryptoAccount.getUuid())) {
            throw new RuntimeException(
                    CRYPTO_ACCOUNT_ID_EXIST.formatted(cryptoAccount.getUuid())
            );
        }
        getStorage().put(cryptoAccount.getUuid(), cryptoAccount);
    }

    public CryptoAccount findByUuid(UUID uuid) throws CryptoAccountNotFoundException {
        return Optional.ofNullable(getStorage().get(uuid))
                .orElseThrow(
                        () -> new CryptoAccountNotFoundException(CRYPTO_ACCOUNT_ID_NOTFOUND.formatted(uuid))
                );
    }

    public List<CryptoAccount> findAllByUserLogin(String userLogin) {
        return getStorage().values().stream()
                .filter(cryptoAccount -> cryptoAccount.getUserLogin().equals(userLogin))
                .toList();
    }

    private Map<UUID, CryptoAccount> getStorage() {
        return cryptoAccountStorage.getCryptoStorage();
    }
}
