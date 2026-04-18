package com.bank.account.domain.exception;

public class InsufficientFundsException extends BusinessException {

    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
