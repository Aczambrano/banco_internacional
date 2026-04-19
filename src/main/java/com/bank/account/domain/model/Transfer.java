package com.bank.account.domain.model;

import com.bank.account.domain.model.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transfer {

    private Long id;
    private Long sourceAccountId;
    private Long targetAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private String reference;
    private LocalDateTime createdAt;

    public Transfer(Long id,
                    Long sourceAccountId,
                    Long targetAccountId,
                    BigDecimal amount,
                    String reference) {

        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.status = TransactionStatus.PENDING;
        this.reference = reference;
        this.createdAt = LocalDateTime.now();
    }

    public Transfer(Long id,
                    Long sourceAccountId,
                    Long targetAccountId,
                    BigDecimal amount,
                    TransactionStatus status,
                    String reference,
                    LocalDateTime createdAt) {
        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
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

    public Long getId() { return id; }
    public Long getSourceAccountId() { return sourceAccountId; }
    public Long getTargetAccountId() { return targetAccountId; }
    public BigDecimal getAmount() { return amount; }
    public TransactionStatus getStatus() { return status; }
    public String getReference() { return reference; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
