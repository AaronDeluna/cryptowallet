package org.javaacademy.cryptowallet.service.crypto;

import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.javaacademy.cryptowallet.exception.CryptoPriceRetrievalException;

import java.io.IOException;
import java.math.BigDecimal;

public interface CryptoPriceService {
    BigDecimal getCryptoPriceByCurrency(CryptoCurrency currency) throws CryptoPriceRetrievalException;
}
