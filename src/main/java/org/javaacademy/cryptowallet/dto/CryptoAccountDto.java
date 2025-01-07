package org.javaacademy.cryptowallet.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CryptoAccountDto {
    @Schema(description = "Уникальный id")
    private UUID uuid;
    @Schema(description = "Логин пользователя")
    private String userLogin;
    @Schema(description = "Тип крипто-валюты")
    private String currency;
    @Schema(description = "Количество крипто-валюты")
    private BigDecimal currencyCount;
}
