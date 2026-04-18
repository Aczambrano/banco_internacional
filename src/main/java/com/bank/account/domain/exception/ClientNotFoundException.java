package com.bank.account.domain.exception;

public class ClientNotFoundException extends BusinessException {

    public ClientNotFoundException(Long id) {
        super("Customer not found with id: " + id);
    }
}