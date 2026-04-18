package com.bank.account.domain.exception;

public class InvalidAccountTypeException extends BusinessException {

    public InvalidAccountTypeException(String accountType) {
        super("Invalid account type: " + accountType);
    }
}