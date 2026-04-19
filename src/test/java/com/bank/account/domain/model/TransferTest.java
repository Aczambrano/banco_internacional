package com.bank.account.domain.model;

import com.bank.account.domain.model.enums.TransactionStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransferTest {

    @Test
    void shouldCreateTransferWithDefaultValues() {
        Transfer transfer = new Transfer(
                1L,
                100L,
                200L,
                new BigDecimal("150.00"),
                "123"
        );

        assertEquals(1L, transfer.getId());
        assertEquals(100L, transfer.getSourceAccountId());
        assertEquals(200L, transfer.getTargetAccountId());
        assertEquals(new BigDecimal("150.00"), transfer.getAmount());

        assertEquals(TransactionStatus.PENDING, transfer.getStatus());
        assertNotNull(transfer.getReference());
        assertNotNull(transfer.getCreatedAt());
    }

    @Test
    void shouldCreateTransferWithProvidedValues() {
        LocalDateTime now = LocalDateTime.now();

        Transfer transfer = new Transfer(
                2L,
                300L,
                400L,
                new BigDecimal("500.00"),
                TransactionStatus.SUCCESS,
                "REF-XYZ",
                now
        );

        assertEquals(2L, transfer.getId());
        assertEquals(300L, transfer.getSourceAccountId());
        assertEquals(400L, transfer.getTargetAccountId());
        assertEquals(new BigDecimal("500.00"), transfer.getAmount());

        assertEquals(TransactionStatus.SUCCESS, transfer.getStatus());
        assertEquals("REF-XYZ", transfer.getReference());
        assertEquals(now, transfer.getCreatedAt());
    }

    @Test
    void shouldMarkTransferAsSuccess() {
        Transfer transfer = createPendingTransfer();

        transfer.markAsSuccess();

        assertEquals(TransactionStatus.SUCCESS, transfer.getStatus());
    }

    @Test
    void shouldMarkTransferAsFailed() {
        Transfer transfer = createPendingTransfer();

        transfer.markAsFailed();

        assertEquals(TransactionStatus.FAILED, transfer.getStatus());
    }

    private Transfer createPendingTransfer() {
        return new Transfer(
                10L,
                111L,
                222L,
                new BigDecimal("75.00"),
                "123"
        );
    }
}