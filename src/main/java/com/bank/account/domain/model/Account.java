package com.bank.account.domain.model;

import com.bank.account.domain.exception.AccountNotActiveException;
import com.bank.account.domain.exception.InsufficientFundsException;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;

import java.math.BigDecimal;

public class Account {

    private Long id;
    private String accountNumber;
    private Long clientId;
    private AccountType type;
    private BigDecimal balance;
    private AccountStatus status;

    public Account(Long id,
                   String accountNumber,
                   Long clientId,
                   AccountType type,
                   BigDecimal balance,
                   AccountStatus status) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.clientId = clientId;
        this.type = type;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.status = status;
    }

    public void deposit(BigDecimal amount) {
        validateActive();
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateActive();
        validateAmount(amount);

        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }

        this.balance = this.balance.subtract(amount);
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }

    private void validateActive() {
        if (!isActive()) {
            throw new AccountNotActiveException();
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public Long getClientId() { return clientId; }
    public AccountType getType() { return type; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
}