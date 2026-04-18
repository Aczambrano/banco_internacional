package com.bank.account.application.dto.request;

import java.math.BigDecimal;

public record TransferRequest(
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount
) {}