package org.javaacademy.cryptowallet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.dto.ErrorResponse;
import org.javaacademy.cryptowallet.dto.RefillRequestDto;
import org.javaacademy.cryptowallet.dto.WithdrawalRequestDto;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cryptowallet")
@RequiredArgsConstructor
@Tag(
        name = "Crypto account controller",
        description = "Контроллер для работы с крипто-кошельком"
)
public class CryptoAccountController {
    private final CryptoAccountService cryptoAccountService;

    @Operation(
            summary = "Получение всех крипто-кошельков",
            description = "Получение всех крипто кошельков по логину пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CryptoAccountDto.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    @Cacheable(value = "cryptoAccount")
    public ResponseEntity<List<CryptoAccountDto>> getAllCryptoAccountByUserLogin(
            @RequestParam(name = "user_login") String userLogin) {
        return ResponseEntity.ok().body(cryptoAccountService.getAllCryptoAccountByUserLogin(userLogin));
    }

    @Operation(
            summary = "Создание нового крипто-кошелька",
            description = "Создает новый крипто-кошелек для пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Создан",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UUID.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<UUID> createCryptoAccount(@Valid @RequestBody CreateCryptoAccountDto cryptoAccountDto) {
        UUID uuid = cryptoAccountService.createCryptoAccount(cryptoAccountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(uuid);
    }

    @Operation(
            summary = "Пополнение счета в рублях",
            description = "Пополняет счет крипто-кошелька"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Счет не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/refill")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<HttpStatus> replenishAccountInRubles(@RequestBody RefillRequestDto refillRequestDto) {
        cryptoAccountService.replenishAccountInRubles(refillRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Снятие рублей со счета",
            description = "Снимает рубли с счета крипто-кошелька"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(
                            mediaType = "plain/text",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Счет не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/withdrawal")
    @CacheEvict(value = "cryptoAccount", allEntries = true)
    public ResponseEntity<String> withdrawRublesFromAccount(@RequestBody WithdrawalRequestDto withdrawalRequestDto) {
        return ResponseEntity.ok().body(cryptoAccountService.withdrawRublesFromAccount(withdrawalRequestDto));
    }

    @Operation(
            summary = "Показывает рублевый эквивалент криптосчета по id",
            description = "Показывает рублевый эквивалент криптосчета по id крипто-кошелька")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(
                            mediaType = "plain/text",
                            schema = @Schema(implementation = BigDecimal.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Счет не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/balance/{id}")
    @Cacheable(value = "cryptoAccountBalance")
    public ResponseEntity<BigDecimal> showAccountBalanceInRublesById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(cryptoAccountService.showAccountBalanceInRublesById(id));
    }

    @Operation(
            summary = "Показывает рублевый эквивалент всех крипто счетов",
            description = "Показывает рублевый эквивалент всех крипто счетов по логину пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешно",
                    content = @Content(
                            mediaType = "plain/text",
                            schema = @Schema(implementation = BigDecimal.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Внутренняя ошибка сервера",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/balance/user/{login}")
    @Cacheable(value = "AllCryptoAccountBalance")
    public ResponseEntity<BigDecimal> showAllAccountBalanceInRublesByUserLogin(@PathVariable String login) {
        return ResponseEntity.ok().body(cryptoAccountService.showAllAccountBalanceInRublesByUserLogin(login));
    }
}
