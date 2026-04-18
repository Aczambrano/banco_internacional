package com.bank.account.domain.service;

import com.bank.account.domain.exception.AccountNotActiveException;
import com.bank.account.domain.exception.InsufficientFundsException;
import com.bank.account.domain.model.Account;

import java.math.BigDecimal;

public class AccountDomainService {

    public void validateDebit(Account account, BigDecimal amount) {
        if (!account.isActive()) {
            throw new AccountNotActiveException();
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
    }

    public void validateCredit(Account account) {
        if (!account.isActive()) {
            throw new AccountNotActiveException();
        }
    }

    public void transfer(Account source, Account target, BigDecimal amount) {
        validateDebit(source, amount);
        validateCredit(target);

        source.withdraw(amount);
        target.deposit(amount);
    }
}