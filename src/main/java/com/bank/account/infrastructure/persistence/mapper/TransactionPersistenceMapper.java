package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.domain.model.Transaction;
import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransactionPersistenceMapper {

    TransactionEntity toEntity(Transaction domain);

    TransactionResponse toResponse(Transaction domain);

    Transaction toResponse(TransactionEntity domain);

}