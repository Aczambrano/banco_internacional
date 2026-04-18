package com.bank.account.domain.exception;

public class AccountNotFoundException extends BusinessException {

    public AccountNotFoundException(Long id) {
        super("Account not found with id: " + id);
    }
}