package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.domain.model.Transaction;
import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {

    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Transaction toDomain(TransactionEntity entity);

    TransactionEntity toEntity(Transaction domain);

    // 🔥 lógica adicional post-mapping
    @AfterMapping
    default void mapStatus(TransactionEntity entity, @MappingTarget Transaction domain) {

        if (entity.getStatus() == null) return;

        switch (entity.getStatus()) {
            case SUCCESS -> domain.markAsSuccess();
            case FAILED -> domain.markAsFailed();
            case REVERSED -> domain.markAsReversed();
        }
    }
}