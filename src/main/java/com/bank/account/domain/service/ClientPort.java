package com.bank.account.domain.service;

public interface ClientPort {
    void validateClientExists(Long clientId);
}