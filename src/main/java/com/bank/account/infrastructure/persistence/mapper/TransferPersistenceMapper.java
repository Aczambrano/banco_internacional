package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.domain.model.Transfer;
import com.bank.account.infrastructure.persistence.entity.TransferEntity;
import org.springframework.stereotype.Component;


@Component
public class TransferPersistenceMapper {

    public TransferEntity toEntity(Transfer domain) {
        TransferEntity entity = new TransferEntity();
        entity.setId(domain.getId());
        entity.setSourceAccountId(domain.getSourceAccountId());
        entity.setTargetAccountId(domain.getTargetAccountId());
        entity.setAmount(domain.getAmount());
        entity.setStatus(domain.getStatus());
        entity.setReference(domain.getReference());
        entity.setCreatedAt(domain.getCreatedAt());
        return entity;
    }

    public Transfer toResponse(TransferEntity entity) {
        return new Transfer(
                entity.getId(),
                entity.getSourceAccountId(),
                entity.getTargetAccountId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getReference(),
                entity.getCreatedAt()
        );
    }
}