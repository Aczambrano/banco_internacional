package com.bank.account.application.usecase;

import com.bank.account.domain.exception.InvalidAccountTypeException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
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

        try {
            Account account = new Account(
                    null,
                    generateAccountNumber(),
                    clientId,
                    AccountType.valueOf(type),
                    BigDecimal.ZERO,
                    AccountStatus.ACTIVE,
                    CurrencyCode.USD
            );
            return accountRepository.save(account);

        }catch (IllegalArgumentException e) {
            throw new InvalidAccountTypeException(type);
        }

    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}