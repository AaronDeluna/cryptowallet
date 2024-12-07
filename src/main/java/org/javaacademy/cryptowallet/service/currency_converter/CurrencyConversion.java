package org.javaacademy.cryptowallet.service.currency_converter;

import java.io.IOException;
import java.math.BigDecimal;

public interface CurrencyConversion {
    BigDecimal convertDollarToRubles(BigDecimal dollarCount) throws IOException;

    BigDecimal convertRubleToDollar(BigDecimal rubleCount) throws IOException;
}
