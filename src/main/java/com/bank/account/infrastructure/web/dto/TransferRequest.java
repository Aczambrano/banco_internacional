package com.bank.account.infrastructure.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest(

        @NotNull(message = "Source account id is required")
        @Positive(message = "Source account id must be greater than 0")
        Long sourceAccountId,

        @NotNull(message = "Target account id is required")
        @Positive(message = "Target account id must be greater than 0")
        Long targetAccountId,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Reference is required")
        @Size(max = 10, message = "Reference must not exceed 10 characters")
        String reference

) {

    public void validateDifferentAccounts() {
        if (sourceAccountId != null &&
                targetAccountId != null &&
                sourceAccountId.equals(targetAccountId)) {

            throw new IllegalArgumentException("Source and target accounts must be different");
        }
    }
}