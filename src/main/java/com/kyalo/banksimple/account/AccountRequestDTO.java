package com.kyalo.banksimple.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AccountRequestDTO(
        @NotNull(message = "Account holder name is required") String accountHolder,
        @Positive(message = "Initial balance must be positive")BigDecimal initialBalance) {
}
