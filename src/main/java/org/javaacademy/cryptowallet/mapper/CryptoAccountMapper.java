package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

@Component
public class CryptoAccountMapper {

    public CryptoAccount convertToEntity(CryptoAccountDto dto) {
        return new CryptoAccount(
                dto.getUserLogin(),
                dto.getCurrency()
        );
    }

    public CryptoAccountDto convertToDto(CryptoAccount cryptoAccount) {
        return new CryptoAccountDto(
                cryptoAccount.getUserLogin(),
                cryptoAccount.getCurrency()
        );
    }
}
