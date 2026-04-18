package com.bank.account.application.dto.response;

import com.bank.account.domain.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long id,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        TransactionStatus status,
        String reference,
        LocalDateTime createdAt
) {

}