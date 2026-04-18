package com.bank.account.application.dto.response;

import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String accountNumber,
        Long clientId,
        AccountType type,
        BigDecimal balance,
        AccountStatus status,
        CurrencyCode currency
) {}