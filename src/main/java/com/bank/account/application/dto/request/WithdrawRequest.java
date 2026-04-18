package com.bank.account.application.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(
        Long accountId,
        BigDecimal amount
) {}
