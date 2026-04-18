package com.bank.account.domain.exception;

public class AccountNotActiveException extends BusinessException {

    public AccountNotActiveException() {
        super("Account is not active");
    }
}