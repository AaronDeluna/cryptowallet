package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class CryptoAccountMapper {

    public CryptoAccount convertToEntity(CreateCryptoAccountDto createDto) {
        return new CryptoAccount(
                UUID.randomUUID(),
                createDto.getUserLogin(),
                createDto.getCurrency(),
                BigDecimal.ZERO
        );
    }

    public CryptoAccountDto convertToDto(CryptoAccount cryptoAccount) {
        return new CryptoAccountDto(
                cryptoAccount.getUuid(),
                cryptoAccount.getUserLogin(),
                cryptoAccount.getCurrency(),
                cryptoAccount.getCurrencyCount()
        );
    }
}
