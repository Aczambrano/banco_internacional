package com.bank.account.application.port.out;

public interface ClientPort {
    void validateClientExists(Long clientId);
}