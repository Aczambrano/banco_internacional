package com.bank.account.application.dto.response;

import com.bank.account.domain.model.enums.TransactionStatus;
import com.bank.account.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        Long accountId,
        BigDecimal amount,
        TransactionType type,
        TransactionStatus status,
        String reference,
        LocalDateTime createdAt
) {}