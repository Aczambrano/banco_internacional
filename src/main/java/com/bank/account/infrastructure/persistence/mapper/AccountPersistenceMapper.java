package com.bank.account.infrastructure.persistence.mapper;

import com.bank.account.domain.model.Account;
import com.bank.account.infrastructure.persistence.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AccountPersistenceMapper {

    Account toDomain(AccountEntity entity);

    @Mapping(target = "createdAt", ignore = true)
    AccountEntity toEntity(Account domain);
}