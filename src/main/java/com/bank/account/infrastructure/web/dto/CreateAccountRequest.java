package com.bank.account.infrastructure.web.dto;

import com.bank.account.domain.model.enums.AccountType;
import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(

        @NotNull(message = "ClientId is required")
        Long clientId,

        @NotNull(message = "Account type is required")
        AccountType type

) {}