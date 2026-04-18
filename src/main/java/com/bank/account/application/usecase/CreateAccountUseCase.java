package com.bank.account.application.usecase;

import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;

    public CreateAccountUseCase(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account execute(Long clientId, String type) {

        Account account = new Account(
                null,
                generateAccountNumber(),
                clientId,
                Enum.valueOf(AccountType.class, type),
                BigDecimal.ZERO,
                AccountStatus.ACTIVE
        );

        accountRepository.save(account);

        return account;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}