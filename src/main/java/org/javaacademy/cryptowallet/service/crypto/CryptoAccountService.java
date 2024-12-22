package org.javaacademy.cryptowallet.service.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.javaacademy.cryptowallet.service.currency_converter.CurrencyConversion;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoAccountService {
    private static final String INSUFFICIENT_FUNDS_MESSAGE = "Ошибка: На счёте недостаточно средств!";
    private static final String OPERATION_SUCCESS_SOLD_MESSAGE = "Операция прошла успешно. Продано: %s";
    private static final int SCALE = 8;
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserStorageRepository userStorageRepository;
    private final CryptoAccountMapper cryptoAccountMapper;
    private final CryptoPriceService cryptoPriceService;
    private final CurrencyConversion currencyConversion;

    public List<CryptoAccountDto> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoAccountRepository.findAllByUserLogin(userLogin).stream()
                .map(cryptoAccountMapper::toDto)
                .toList();
    }

    public UUID createCryptoAccount(CreateCryptoAccountDto createDto) throws UserNotFoundException {
        userStorageRepository.findByLogin(createDto.getUserLogin());
        CryptoAccount cryptoAccount = cryptoAccountMapper.toEntity(createDto);
        cryptoAccountRepository.save(cryptoAccount);
        return cryptoAccount.getUuid();
    }

    public void replenishAccountInRubles(UUID cryptoAccountId, BigDecimal rubleCount)
            throws IOException, CryptoAccountNotFoundException {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(cryptoAccountId)
        );
        CryptoCurrency cryptoCurrency = cryptoAccountDto.getCurrency();
        BigDecimal cryptoPrice = cryptoPriceService.getCryptoPriceByCurrency(cryptoCurrency);
        BigDecimal dollarAmount = currencyConversion.convertRubleToDollar(rubleCount);
        BigDecimal updateCryptoBalance = dollarAmount.divide(cryptoPrice, SCALE, RoundingMode.HALF_UP);
        cryptoAccountRepository.updateBalanceByUuid(cryptoAccountDto.getUuid(), updateCryptoBalance);
    }

    //TODO Задача 1.0 не верно работает логика, переписать!
    public String withdrawRublesFromAccount(UUID cryptoAccountId, BigDecimal rubleCount)
            throws CryptoAccountNotFoundException, IOException {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(cryptoAccountId)
        );
        BigDecimal cryptoAmount = calculateCryptoAmount(rubleCount, cryptoAccountDto.getCurrency());
        validateSufficientFunds(cryptoAccountDto, rubleCount);
        updateAccountBalanceAfterWithdrawal(cryptoAccountDto, cryptoAmount);
        return OPERATION_SUCCESS_SOLD_MESSAGE.formatted(cryptoAmount);
    }

    /**
     * Вернет оставшеся количество рублей на конкретном счету
     *
     * @param cryptoAccountId
     * @return
     * @throws CryptoAccountNotFoundException
     * @throws IOException
     */
    public BigDecimal showAccountBalanceInRublesById(UUID cryptoAccountId)
            throws CryptoAccountNotFoundException, IOException {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(cryptoAccountId)
        );
        BigDecimal rubleAmount = getRubleAmountByCryptoAccount(cryptoAccountDto);
        return rubleAmount;
    }

    /**
     * Показывает остаток рублей на всех крипто кошельках
     *
     * @param userLogin
     * @return
     */
    public BigDecimal showAllAccountBalanceInRublesByUserLogin(String userLogin) {
        AtomicReference<BigDecimal> totalRubleAmount = new AtomicReference<>(BigDecimal.ZERO);
        List<CryptoAccountDto> cryptoAccountsDto = cryptoAccountMapper.toDtos(
                cryptoAccountRepository.findAllByUserLogin(userLogin)
        );

        cryptoAccountsDto.forEach(cryptoAccount ->
                totalRubleAmount.set(totalRubleAmount.get().add(getRubleAmountByCryptoAccount(cryptoAccount)))
        );

        return totalRubleAmount.get();
    }

    private BigDecimal getRubleAmountByCryptoAccount(CryptoAccountDto cryptoAccountDto) {
        try {
            CryptoCurrency cryptoCurrency = cryptoAccountDto.getCurrency();
            BigDecimal cryptoDollarPrice = cryptoPriceService.getCryptoPriceByCurrency(cryptoCurrency);
            BigDecimal resultTotalDollar = cryptoAccountDto.getCurrencyCount().multiply(cryptoDollarPrice);
            return currencyConversion.convertDollarToRubles(resultTotalDollar);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private BigDecimal calculateCryptoAmount(BigDecimal rubleCount, CryptoCurrency cryptoCurrency) throws IOException {
        BigDecimal cryptoCurrencyPrice = cryptoPriceService.getCryptoPriceByCurrency(cryptoCurrency);
        BigDecimal rubleAmount = currencyConversion.convertRubleToDollar(rubleCount);
        BigDecimal totalDollarAmount = currencyConversion.convertDollarToRubles(rubleAmount);
        return totalDollarAmount.divide(cryptoCurrencyPrice, SCALE, RoundingMode.HALF_UP);
    }

    //TODO Задача 1.0 Переписать логику, тоже не верно
    private void validateSufficientFunds(CryptoAccountDto cryptoAccountDto, BigDecimal rubleCount) {

        if (cryptoAccountDto.getCurrencyCount().compareTo(rubleCount) < 0) {
            throw new RuntimeException(INSUFFICIENT_FUNDS_MESSAGE);
        }
    }

    private void updateAccountBalanceAfterWithdrawal(CryptoAccountDto cryptoAccountDto, BigDecimal cryptoAmount) {
        cryptoAccountDto.setCurrencyCount(cryptoAccountDto.getCurrencyCount().subtract(cryptoAmount));
    }

}
