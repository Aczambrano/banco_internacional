package com.bank.account.domain.exception;


public class DuplicateTransactionException extends BusinessException {

    public DuplicateTransactionException(String endToEndId) {
        super("Transaction with End To End Id already exists: " + endToEndId);
    }
}