package org.javaacademy.cryptowallet.web.controller;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.extern.slf4j.Slf4j;
import org.javaacademy.cryptowallet.dto.CreateCryptoAccountDto;
import org.javaacademy.cryptowallet.dto.CryptoAccountDto;
import org.javaacademy.cryptowallet.dto.RefillRequestDto;
import org.javaacademy.cryptowallet.dto.UserDto;
import org.javaacademy.cryptowallet.dto.WithdrawalRequestDto;
import org.javaacademy.cryptowallet.entity.CryptoAccount;
import org.javaacademy.cryptowallet.entity.CryptoCurrency;
import org.javaacademy.cryptowallet.mapper.CryptoAccountMapper;
import org.javaacademy.cryptowallet.service.crypto.CryptoAccountService;
import org.javaacademy.cryptowallet.service.user.UserService;
import org.javaacademy.cryptowallet.storage.CryptoAccountStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.javaacademy.cryptowallet.entity.CryptoCurrency.BTC;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@DisplayName("Тест контроллера криптосчета")
@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CryptoAccountControllerTest {
    @Autowired
    private CryptoAccountService cryptoAccountService;
    @Autowired
    private UserService userService;
    @Autowired
    private CryptoAccountStorage cryptoAccountStorage;
    @Autowired
    private CryptoAccountMapper cryptoAccountMapper;
    private final RequestSpecification requestSpecification = new RequestSpecBuilder()
            .setBasePath("/cryptowallet")
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private final ResponseSpecification responseSpecification = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();

    @Test
    @DisplayName("Успешное получение всех крипто кошельков пользователя по логину")
    public void getAllCryptoAccountByUserLoginSuccess() {
        String expectedName = "Anton";

        UUID expectedUuid = cryptoAccountService.createCryptoAccount(CreateCryptoAccountDto.builder()
                .userLogin(expectedName)
                .currency(BTC)
                .build()
        );

        CryptoAccountDto expectedCryptoAccountDto = CryptoAccountDto.builder()
                .uuid(expectedUuid)
                .userLogin(expectedName)
                .currency(BTC)
                .currencyCount(BigDecimal.ZERO)
                .build();

        String url = "cryptowallet?user_login=%s".formatted(expectedName);
        List<CryptoAccountDto> cryptoAccountDtos = given()
                .get(url)
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .body()
                .as(new TypeRef<>() {});

        int expectedSize = 1;
        int resultSize = cryptoAccountDtos.size();
        CryptoAccountDto resultCryptoAccountDto = cryptoAccountDtos.get(0);

        assertEquals(expectedSize, resultSize);
        assertEquals(expectedCryptoAccountDto, resultCryptoAccountDto);
    }

    @Test
    @DisplayName("Успешное создание нового крипто-кошелька")
    public void createCryptoAccountSuccess() {
        String expectedUserLogin = "Anton";
        CryptoCurrency expectedCryptoCurrency = BTC;

        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin(expectedUserLogin)
                .currency(expectedCryptoCurrency)
                .build();

        UUID cryptoAccountId = given(requestSpecification)
                .body(createCryptoAccountDto)
                .post()
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .extract()
                .as(UUID.class);

        CryptoAccountDto resultCryptoAccountDto = cryptoAccountMapper.toDto(
                cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId)
        );

        String resultUserLogin = resultCryptoAccountDto.getUserLogin();
        CryptoCurrency resultCryptoCurrency = resultCryptoAccountDto.getCurrency();
        int resultCurrencyCount =  ZERO.compareTo(resultCryptoAccountDto.getCurrencyCount());

        assertEquals(expectedUserLogin, resultUserLogin);
        assertEquals(expectedCryptoCurrency, resultCryptoCurrency);
        assertEquals(0, resultCurrencyCount);

    }

    @Test
    @DisplayName("Успешное пополнение крипто-счета, рублями")
    public void replenishAccountInRublesSuccess() {
        String expectedUserLogin = "Anton";

        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin(expectedUserLogin)
                .currency(BTC)
                .build();

        UUID cryptoAccountId = cryptoAccountService.createCryptoAccount(createCryptoAccountDto);

        RefillRequestDto refillRequestDto = RefillRequestDto.builder()
                .accountId(cryptoAccountId)
                .rubleAmount(valueOf(100))
                .build();

        given(requestSpecification)
                .body(refillRequestDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(200);

        CryptoAccount cryptoAccount = cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId);

        int resultCurrencyCount = ONE.compareTo(cryptoAccount.getCurrencyCount());
        assertEquals(0, resultCurrencyCount);
    }

    @Test
    @DisplayName("Успешное возвращение статуса 400, при попытке пополнить несуществующий крипто-счет по id")
    public void shouldReturnBadRequestWhenCryptoAccountDoesNotExistForTopUpById() {
        RefillRequestDto refillRequestDto = RefillRequestDto.builder()
                .accountId(UUID.randomUUID())
                .rubleAmount(valueOf(100))
                .build();

        given(requestSpecification)
                .body(refillRequestDto)
                .post("/refill")
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }

    @Test
    @DisplayName("Успешное снятие средств с крипто-счет по id")
    public void withdrawRublesFromAccountSuccess() {
        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin("Anton")
                .currency(BTC)
                .build();

        UUID cryptoAccountId = cryptoAccountService.createCryptoAccount(createCryptoAccountDto);
        CryptoAccount cryptoAccount = cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId);
        cryptoAccount.setCurrencyCount(valueOf(1));

        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(cryptoAccountId)
                .rubleAmount(valueOf(100))
                .build();

        given(requestSpecification)
                .body(withdrawalRequestDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(200);

        int resultCurrencyCount = ZERO.compareTo(cryptoAccount.getCurrencyCount());
        assertEquals(0, resultCurrencyCount);
    }

    @Test
    @DisplayName("Успешное возвращение статуса 400, при попытке снять средства с несуществующего крипто-счет по id")
    public void shouldReturnBadRequestWhenCryptoAccountDoesNotExistForWithdrawalById() {
        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(UUID.randomUUID())
                .rubleAmount(valueOf(100))
                .build();

        given(requestSpecification)
                .body(withdrawalRequestDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }

    @Test
    @DisplayName("Успешное возвращение статуса 400, в случае проведения операции с некорректной суммой на аккаунте")
    public void shouldReturnBadRequestWhenAmountIsInvalidForOperationOnAccount() {
        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin("Anton")
                .currency(BTC)
                .build();

        UUID cryptoAccountId = cryptoAccountService.createCryptoAccount(createCryptoAccountDto);
        CryptoAccount cryptoAccount = cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId);
        cryptoAccount.setCurrencyCount(valueOf(1));

        WithdrawalRequestDto withdrawalRequestDto = WithdrawalRequestDto.builder()
                .accountId(cryptoAccountId)
                .rubleAmount(valueOf(1000))
                .build();

        given(requestSpecification)
                .body(withdrawalRequestDto)
                .post("/withdrawal")
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }

    @Test
    @DisplayName("Успешно показывает рублевый эквивалент криптосчета по id")
    public void showAccountBalanceInRublesByIdSuccess() {
        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin("Anton")
                .currency(BTC)
                .build();

        UUID cryptoAccountId = cryptoAccountService.createCryptoAccount(createCryptoAccountDto);
        CryptoAccount cryptoAccount = cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId);
        cryptoAccount.setCurrencyCount(valueOf(1));

        BigDecimal resultRubleAmount = given(requestSpecification)
                .get("/balance/%s".formatted(cryptoAccountId))
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .as(BigDecimal.class);

        BigDecimal expectedRubleAmount = valueOf(100);
        assertEquals(0, expectedRubleAmount.compareTo(resultRubleAmount));
    }

    @Test
    @DisplayName("Успешное возвращение статуса 400, в случае если запрашивашивается"
            + " рублевый эквивалет у несуществующего аккаунта")
    public void shouldReturnBadRequestWhenRequestingBalanceForNonExistentAccount() {
        UUID cryptoAccountId = UUID.randomUUID();
        given(requestSpecification)
                .get("/balance/%s".formatted(cryptoAccountId))
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }

    @Test
    @DisplayName("Успешно показывает рублевый эквивалент всех крипто счетов")
    public void showAllAccountBalanceInRublesByUserLogin() {
        String expectedLogin = "Anton";

        userService.save(UserDto.builder()
                .userLogin(expectedLogin)
                .email("aa@mail.ru")
                .password("1234")
                .build()
        );

        CreateCryptoAccountDto createCryptoAccountDto = CreateCryptoAccountDto.builder()
                .userLogin(expectedLogin)
                .currency(BTC)
                .build();

        for (int i = 0; i < 2; i++) {
            UUID cryptoAccountId = cryptoAccountService.createCryptoAccount(createCryptoAccountDto);
            CryptoAccount cryptoAccount = cryptoAccountStorage.getCryptoStorage().get(cryptoAccountId);
            cryptoAccount.setCurrencyCount(valueOf(1));
        }

        BigDecimal resultRubleAmount = given(requestSpecification)
                .get("/balance/user/%s".formatted(expectedLogin))
                .then()
                .spec(responseSpecification)
                .statusCode(200)
                .extract()
                .as(BigDecimal.class);

        BigDecimal expectedRubleAmount = valueOf(200);
        assertEquals(0, expectedRubleAmount.compareTo(resultRubleAmount));
    }

    @Test
    @DisplayName("Успешное возвращение статуса 400, если при запросе эквивалента всех "
            + "криптокошельков указанный логин не существует.")
    public void shouldReturnBadRequestWhenLoginDoesNotExist() {
        given(requestSpecification)
                .get("/balance/user/%s".formatted("Dima"))
                .then()
                .spec(responseSpecification)
                .statusCode(400);
    }
}
