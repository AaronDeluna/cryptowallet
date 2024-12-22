package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RefillRequestDto {
    @JsonProperty("account_id")
    private UUID accountId;
    @JsonProperty("rubles_amount")
    private BigDecimal rubleAmount;
}
