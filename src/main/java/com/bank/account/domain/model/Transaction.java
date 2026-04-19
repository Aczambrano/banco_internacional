package com.bank.account.domain.model;

import com.bank.account.domain.model.enums.TransactionStatus;
import com.bank.account.domain.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {

    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private String reference;
    private LocalDateTime createdAt;

    public Transaction(Long id,
                       Long accountId,
                       BigDecimal amount,
                       TransactionType type,
                       String reference) {

        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.status = TransactionStatus.PENDING;
        this.reference = reference;
        this.createdAt = LocalDateTime.now();
    }

    public Transaction(Long id,
                       Long accountId,
                       BigDecimal amount,
                       TransactionType type,
                       TransactionStatus status,
                       String reference,
                       LocalDateTime createdAt) {

        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.type = type;
        this.status = status;
        this.reference = reference;
        this.createdAt = createdAt;
    }

    public void markAsSuccess() {
        this.status = TransactionStatus.SUCCESS;
    }

    public void markAsFailed() {
        this.status = TransactionStatus.FAILED;
    }

    public void markAsReversed() {
        this.status = TransactionStatus.REVERSED;
    }

    public Long getId() { return id; }
    public Long getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public TransactionStatus getStatus() { return status; }
    public String getReference() { return reference; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}