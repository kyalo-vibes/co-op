package com.kyalo.banksimple.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountResponseDTO(
        @NotNull(message = "Id is required")Long id,
        @NotNull(message = "Account holder name is required")String accountHolder,
        @Positive(message = "Balance must be positive")BigDecimal balance) {
}
