package org.javaacademy.cryptowallet.service.crypto;

import org.javaacademy.cryptowallet.exception.CryptoPriceRetrievalException;

import java.math.BigDecimal;

public interface CryptoPriceService {
    BigDecimal getCryptoPriceByCurrency(String currency) throws CryptoPriceRetrievalException;
}
