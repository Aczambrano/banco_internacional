package com.bank.account.domain.exception;


public class DuplicateTransactionException extends BusinessException {

    public DuplicateTransactionException(String reference) {
        super("Transaction with reference already exists: " + reference);
    }
}