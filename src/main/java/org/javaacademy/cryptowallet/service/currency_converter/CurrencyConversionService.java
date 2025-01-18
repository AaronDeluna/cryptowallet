package org.javaacademy.cryptowallet.service.currency_converter;

import java.math.BigDecimal;

public interface CurrencyConversionService {
    BigDecimal convertDollarToRubles(BigDecimal dollarCount);

    BigDecimal convertRubleToDollar(BigDecimal rubleCount);
}
