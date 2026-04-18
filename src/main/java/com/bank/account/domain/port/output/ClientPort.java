package com.bank.account.domain.port.output;

public interface ClientPort {
    void validateClientExists(Long clientId);
}