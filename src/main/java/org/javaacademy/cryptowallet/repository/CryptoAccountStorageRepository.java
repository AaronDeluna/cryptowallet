package org.javaacademy.cryptowallet.repository;

import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CryptoAccountStorageRepository {
    private final CryptoAccountStorage cryptoAccountStorage;

    public void save(CryptoAccount cryptoAccount) {
        cryptoAccountStorage.save(cryptoAccount);
    }

    public CryptoAccount getCryptoAccountByUuid(UUID id) {
        return cryptoAccountStorage.getCryptoAccountByUuid(id);
    }

    public List<CryptoAccount> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoAccountStorage.getAllCryptoAccountByUserLogin(userLogin);
    }
}
