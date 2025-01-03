package org.javaacademy.cryptowallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class WithdrawalRequestDto {
    @Schema(description = "id крипто счета")
    @JsonProperty("account_id")
    private UUID accountId;
    @Schema(description = "Количество рублей")
    @JsonProperty("rubles_amount")
    private BigDecimal rubleAmount;
}
