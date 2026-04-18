package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.domain.model.Transaction;
import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionPersistenceMapper {

    public TransactionEntity toEntity(Transaction domain) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(domain.getId());
        entity.setAccountId(domain.getAccountId());
        entity.setAmount(domain.getAmount());
        entity.setType(domain.getType());
        entity.setStatus(domain.getStatus());
        entity.setReference(domain.getReference());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public Transaction toResponse(TransactionEntity entity) {
        return new Transaction(
                entity.getId(),
                entity.getAccountId(),
                entity.getAmount(),
                entity.getType(),
                entity.getStatus(),
                entity.getReference(),
                entity.getCreatedAt()
        );
    }
}