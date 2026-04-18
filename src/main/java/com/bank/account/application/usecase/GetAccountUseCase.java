package com.bank.account.application.usecase;

import com.bank.account.application.port.in.GetAccountPort;
import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.application.port.out.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAccountUseCase implements GetAccountPort {

    private final AccountRepository accountRepository;

    public GetAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account execute(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }
}
