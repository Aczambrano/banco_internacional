package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Account;
import com.bank.account.domain.repository.AccountRepository;
import com.bank.account.infrastructure.persistence.mapper.AccountPersistenceMapper;
import com.bank.account.infrastructure.persistence.repository.JpaAccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final Logger log = LoggerFactory.getLogger(AccountRepositoryAdapter.class);
    private final JpaAccountRepository jpa;
    private final AccountPersistenceMapper mapper;

    @Override
    public Optional<Account> findById(Long id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByIdForUpdate(Long id) {
        try {
            Optional<Account> acc = jpa.findByIdForUpdate(id).map(mapper::toDomain);
            return acc;
        } catch (Exception e) {
            log.error("Error retrieving account with id: {}", id, e);
            throw new RuntimeException("Error retrieving account for update", e);
        }
    }

    @Override
    public void save(Account account) {
        jpa.save(mapper.toEntity(account));
    }

}