package com.bank.account.domain.repository;

import com.bank.account.domain.model.Account;

import java.util.Optional;

public interface AccountRepository {

    Optional<Account> findById(Long id);

    Optional<Account> findByIdForUpdate(Long id);

    Account save(Account account);
}