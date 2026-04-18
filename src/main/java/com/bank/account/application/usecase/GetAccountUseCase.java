package com.bank.account.application.usecase;

import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.repository.AccountRepository;

public class GetAccountUseCase {

    private final AccountRepository accountRepository;

    public GetAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
