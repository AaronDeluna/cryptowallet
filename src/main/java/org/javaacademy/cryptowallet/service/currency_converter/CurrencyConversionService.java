package org.javaacademy.cryptowallet.service.currency_converter;

import org.javaacademy.cryptowallet.exception.CurrencyConversionException;

import java.io.IOException;
import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertDollarToRubles(BigDecimal dollarCount) throws CurrencyConversionException;

    BigDecimal convertRubleToDollar(BigDecimal rubleCount) throws CurrencyConversionException;
}
