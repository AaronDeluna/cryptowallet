package org.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;

@Data
@AllArgsConstructor
public class CryptoAccountDto {
    private String userLogin;
    private CryptoCurrency currency;
}
