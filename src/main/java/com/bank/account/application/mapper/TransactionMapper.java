package com.bank.account.application.mapper;

import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.domain.model.Transaction;

public class TransactionMapper {

    public static TransactionResponse toResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getAccountId(),
                tx.getAmount(),
                tx.getType(),
                tx.getStatus(),
                tx.getReference(),
                tx.getCreatedAt()
        );
    }
}