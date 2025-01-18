package org.javaacademy.cryptowallet.service.crypto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.dto.RefillRequestDto;
import org.javaacademy.cryptowallet.dto.WithdrawalRequestDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.javaacademy.cryptowallet.exception.InsufficientFundsException;
import org.javaacademy.cryptowallet.exception.InvalidCryptoCurrencyException;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.repository.CryptoAccountRepository;
import org.javaacademy.cryptowallet.repository.UserStorageRepository;
import org.javaacademy.cryptowallet.service.currency_converter.CurrencyConversionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoAccountService {
    private static final String INSUFFICIENT_FUNDS_MESSAGE = "На счету недостаточно средств!";
    private static final String OPERATION_SOLD_MESSAGE = "Операция прошла успешно. Продано: %s, %s";
    private static final String INVALID_CRYPTOCURRENCY_TYPE_ERROR = "Не вреный тип криптовалюты:"
            + " %s доступные валюты %s";
    private static final int SCALE = 8;
    private final CryptoAccountRepository cryptoAccountRepository;
    private final UserStorageRepository userStorageRepository;
    private final CryptoAccountMapper cryptoAccountMapper;
    private final CryptoPriceService cryptoPriceService;
    private final CurrencyConversionService currencyConversionService;

    public List<CryptoAccountDto> getAllCryptoAccountByUserLogin(String userLogin) {
        return cryptoAccountRepository.findAllByUserLogin(userLogin).stream()
                .map(cryptoAccountMapper::toDto)
                .toList();
    }

    public UUID createCryptoAccount(CreateCryptoAccountDto createDto) {
        validateCryptoCurrency(createDto.getCurrency());
        userStorageRepository.findByLogin(createDto.getUserLogin());
        CryptoAccount cryptoAccount = cryptoAccountMapper.toEntity(createDto);
        cryptoAccountRepository.save(cryptoAccount);
        return cryptoAccount.getUuid();
    }

    public void replenishAccountInRubles(RefillRequestDto refillRequestDto) {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(refillRequestDto.getAccountId())
        );
        BigDecimal cryptoAmount = calculateCryptoAmount(
                refillRequestDto.getRubleAmount(), cryptoAccountDto.getCurrency()
        );
        replenishAccountBalance(cryptoAccountDto, cryptoAmount);
    }

    public String withdrawRublesFromAccount(WithdrawalRequestDto withdrawalRequestDto) {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(withdrawalRequestDto.getAccountId())
        );
        BigDecimal cryptoAmount = calculateCryptoAmount(
                withdrawalRequestDto.getRubleAmount(), cryptoAccountDto.getCurrency()
        );
        decreaseAccountBalance(cryptoAccountDto, cryptoAmount);
        return OPERATION_SOLD_MESSAGE.formatted(cryptoAmount.toPlainString(),
                cryptoAccountDto.getCurrency()
        );
    }

    /**
     * Вернет оставшеся количество рублей на конкретном счету
     */
    public BigDecimal showAccountBalanceInRublesById(UUID cryptoAccountId) {
        CryptoAccountDto cryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountRepository.findByUuid(cryptoAccountId)
        );
        return calculateRubleAmount(cryptoAccountDto);
    }

    /**
     * Показывает остаток рублей на всех крипто кошельках
     */
    public BigDecimal showAllAccountBalanceInRublesByUserLogin(String userLogin) {
        userStorageRepository.findByLogin(userLogin);
        List<CryptoAccountDto> cryptoAccountsDto = cryptoAccountMapper.toDtos(
                cryptoAccountRepository.findAllByUserLogin(userLogin)
        );
        BigDecimal totalRubleAmount = BigDecimal.ZERO;
        for (CryptoAccountDto cryptoAccountDto : cryptoAccountsDto) {
            totalRubleAmount = totalRubleAmount.add(calculateRubleAmount(cryptoAccountDto));
        }

        return totalRubleAmount;
    }

    /**
     * Расчитывает рублевый эквивалент криптовалюты
     */
    private BigDecimal calculateRubleAmount(CryptoAccountDto cryptoAccountDto) {
        CryptoCurrency cryptoCurrency = cryptoAccountDto.getCurrency();
        BigDecimal cryptoPrice = cryptoPriceService.getCryptoPriceByCurrency(cryptoCurrency);
        BigDecimal dollarAmount = cryptoAccountDto.getCurrencyCount().multiply(cryptoPrice);
        return currencyConversionService.convertDollarToRubles(dollarAmount);
    }

    /**
     * Расчитывает криптовалютный эквивалент
     */
    private BigDecimal calculateCryptoAmount(BigDecimal rubleCount, CryptoCurrency cryptoCurrency) {
        BigDecimal cryptoPrice = cryptoPriceService.getCryptoPriceByCurrency(cryptoCurrency);
        BigDecimal dollarAmount = currencyConversionService.convertRubleToDollar(rubleCount);
        return dollarAmount.divide(cryptoPrice, SCALE, RoundingMode.HALF_UP);
    }

    private void validateSufficientFunds(CryptoAccountDto cryptoAccountDto, BigDecimal cryptoAmount) {
        if (cryptoAccountDto.getCurrencyCount().compareTo(cryptoAmount) < 0) {
            throw new InsufficientFundsException(INSUFFICIENT_FUNDS_MESSAGE);
        }
    }

    private void replenishAccountBalance(CryptoAccountDto cryptoAccountDto, BigDecimal dollarAmount) {
        CryptoAccount cryptoAccount = cryptoAccountRepository.findByUuid(cryptoAccountDto.getUuid());
        cryptoAccount.setCurrencyCount(cryptoAccount.getCurrencyCount().add(dollarAmount));
    }

    private void decreaseAccountBalance(CryptoAccountDto cryptoAccountDto, BigDecimal cryptoAmount) {
        validateSufficientFunds(cryptoAccountDto, cryptoAmount);
        CryptoAccount cryptoAccount = cryptoAccountRepository.findByUuid(cryptoAccountDto.getUuid());
        cryptoAccount.setCurrencyCount(cryptoAccount.getCurrencyCount().subtract(cryptoAmount));
    }

    private void validateCryptoCurrency(String currency) {
        try {
            CryptoCurrency.valueOf(currency);
        } catch (IllegalArgumentException e) {
            String correctCryptoCurrency = Arrays.toString(CryptoCurrency.values());
            throw new InvalidCryptoCurrencyException(
                    INVALID_CRYPTOCURRENCY_TYPE_ERROR.formatted(currency, correctCryptoCurrency));
        }
    }
}
