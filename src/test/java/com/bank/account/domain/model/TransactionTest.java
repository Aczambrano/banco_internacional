package com.bank.account.domain.model;

import com.bank.account.domain.model.enums.TransactionStatus;
import com.bank.account.domain.model.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldCreateTransactionWithDefaultValues() {
        Transaction tx = new Transaction(
                1L,
                100L,
                new BigDecimal("50.00"),
                TransactionType.DEPOSIT
        );

        assertEquals(1L, tx.getId());
        assertEquals(100L, tx.getAccountId());
        assertEquals(new BigDecimal("50.00"), tx.getAmount());
        assertEquals(TransactionType.DEPOSIT, tx.getType());

        assertEquals(TransactionStatus.PENDING, tx.getStatus());
        assertNotNull(tx.getReference());
        assertNotNull(tx.getCreatedAt());
    }

    @Test
    void shouldCreateTransactionWithProvidedValues() {
        LocalDateTime now = LocalDateTime.now();

        Transaction tx = new Transaction(
                2L,
                200L,
                new BigDecimal("100.00"),
                TransactionType.WITHDRAWAL,
                TransactionStatus.SUCCESS,
                "REF-123",
                now
        );

        assertEquals(2L, tx.getId());
        assertEquals(200L, tx.getAccountId());
        assertEquals(new BigDecimal("100.00"), tx.getAmount());
        assertEquals(TransactionType.WITHDRAWAL, tx.getType());
        assertEquals(TransactionStatus.SUCCESS, tx.getStatus());
        assertEquals("REF-123", tx.getReference());
        assertEquals(now, tx.getCreatedAt());
    }

    @Test
    void shouldMarkTransactionAsSuccess() {
        Transaction tx = createPendingTransaction();

        tx.markAsSuccess();

        assertEquals(TransactionStatus.SUCCESS, tx.getStatus());
    }

    @Test
    void shouldMarkTransactionAsFailed() {
        Transaction tx = createPendingTransaction();

        tx.markAsFailed();

        assertEquals(TransactionStatus.FAILED, tx.getStatus());
    }

    @Test
    void shouldMarkTransactionAsReversed() {
        Transaction tx = createPendingTransaction();

        tx.markAsReversed();

        assertEquals(TransactionStatus.REVERSED, tx.getStatus());
    }

    private Transaction createPendingTransaction() {
        return new Transaction(
                10L,
                999L,
                new BigDecimal("25.00"),
                TransactionType.DEPOSIT
        );
    }
}