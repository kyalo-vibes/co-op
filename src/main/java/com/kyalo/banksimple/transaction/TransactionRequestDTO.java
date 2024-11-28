package com.kyalo.banksimple.transaction;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @Positive(message = "Amount must be positive") BigDecimal amount
) {
}
