package org.javaacademy.cryptowallet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.dto.RefillRequestDto;
import org.javaacademy.cryptowallet.exception.CryptoAccountIdExistException;
import org.javaacademy.cryptowallet.exception.CryptoAccountNotFoundException;
import org.javaacademy.cryptowallet.exception.InsufficientFundsException;
import org.javaacademy.cryptowallet.exception.UserNotFoundException;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
@Tag(name = "Crypto account controller", description = "Контроллер для работы с крипто-кошельком")
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;

    @Operation(summary = "Получение всех крипто-кошельков",
            description = "Получение всех крипто кошельков по логину пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CryptoAccountDto.class)))
            })
    })
    @GetMapping
    @CacheEvict(value = "cryptoAccount")
    public ResponseEntity<?> getAllCryptoAccountByUserLogin(
            @RequestParam(name = "user_login") String userLogin) {
        if (cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin).isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok().body(cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin));
    }

    @Operation(summary = "Создание нового крипто-кошелька",
            description = "Создает новый крипто-кошелек для пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Создан", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))
            }),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка", content = {
                    @Content(mediaType = "plain/text", schema = @Schema(implementation = String.class))
            })
    })
    @PostMapping
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<?> createCryptoAccount(@RequestBody CreateCryptoAccountDto cryptoAccountDto) {
        try {
            UUID uuid = cryptoAccountService.createCryptoAccount(cryptoAccountDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
        } catch (CryptoAccountIdExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Пополнение счета в рублях", description = "Пополняет счет крипто-кошелька")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @PostMapping("/refill")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<?> replenishAccountInRubles(@RequestBody RefillRequestDto refillRequestDto) {
        try {
            cryptoAccountService.replenishAccountInRubles(
                    refillRequestDto.getAccountId(), refillRequestDto.getRubleAmount()
            );
            return ResponseEntity.ok().build();
        } catch (IOException | CryptoAccountNotFoundException e) {
            return ResponseEntity.badRequest()
                    .body("Ошибка: %s".formatted(e.getMessage()));
        }
    }

    @Operation(summary = "Снятие рублей со счета", description = "Снимает рубли с счета крипто-кошелька")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "plain/text", schema = @Schema(implementation = String.class))
            }),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @PostMapping("/withdrawal")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<?> withdrawRublesFromAccount(@RequestBody RefillRequestDto refillRequestDto) {
        try {
            return ResponseEntity.ok().body(cryptoAccountService.withdrawRublesFromAccount(
                    refillRequestDto.getAccountId(), refillRequestDto.getRubleAmount()));
        } catch (IOException | CryptoAccountNotFoundException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Ошибка: %s".formatted(e.getMessage()));
        }
    }

    @Operation(summary = "Показывает рублевый эквивалент криптосчета по id",
            description = "Показывает рублевый эквивалент криптосчета по id крипто-кошелька")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "plain/text", schema = @Schema(implementation = BigDecimal.class))
            }),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @GetMapping("/balance/{id}")
    @Cacheable(value = "cryptoAccount")
    public ResponseEntity<?> showAccountBalanceInRublesById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok().body(cryptoAccountService.showAccountBalanceInRublesById(id));
        } catch (IOException | CryptoAccountNotFoundException e) {
            return ResponseEntity.badRequest().body("Ошибка: %s".formatted(e.getMessage()));
        }
    }

    @Operation(summary = "Показывает рублевый эквивалент всех крипто счетов",
            description = "Показывает рублевый эквивалент всех крипто счетов по логину пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно", content = {
                    @Content(mediaType = "plain/text", schema = @Schema(implementation = BigDecimal.class))
            }),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка")
    })
    @GetMapping("/balance/user/{login}")
    @Cacheable(value = "cryptoAccount")
    public ResponseEntity<?> showAllAccountBalanceInRublesByUserLogin(@PathVariable String login) {
        try {
            return ResponseEntity.ok().body(cryptoAccountService.showAllAccountBalanceInRublesByUserLogin(login));
        } catch (UserNotFoundException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
