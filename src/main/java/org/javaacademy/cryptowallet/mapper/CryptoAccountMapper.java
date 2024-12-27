package org.javaacademy.cryptowallet.mapper;

import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CryptoAccountMapper {

    public CryptoAccount toEntity(CreateCryptoAccountDto createDto) {
        return new CryptoAccount(
                createDto.getUserLogin(),
                createDto.getCurrency()
        );
    }

    public CryptoAccount toEntity(CryptoAccountDto dto) {
        return new CryptoAccount(
                dto.getUuid(),
                dto.getUserLogin(),
                dto.getCurrency(),
                dto.getCurrencyCount()
        );
    }

    public CryptoAccountDto toDto(CryptoAccount cryptoAccount) {
        return new CryptoAccountDto(
                cryptoAccount.getUuid(),
                cryptoAccount.getUserLogin(),
                cryptoAccount.getCurrency(),
                cryptoAccount.getCurrencyCount()
        );
    }

    public List<CryptoAccountDto> toDtos(List<CryptoAccount> cryptoAccounts) {
        return cryptoAccounts.stream().map(cryptoAccount ->
                        new CryptoAccountDto(
                                cryptoAccount.getUuid(),
                                cryptoAccount.getUserLogin(),
                                cryptoAccount.getCurrency(),
                                cryptoAccount.getCurrencyCount()))
                .toList();
    }
}
