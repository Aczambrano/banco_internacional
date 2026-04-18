package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.domain.model.Transfer;
import com.bank.account.infrastructure.persistence.entity.TransferEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransferPersistenceMapper {

    TransferEntity toEntity(Transfer domain);

    Transfer toDomain(TransferEntity entity);
}