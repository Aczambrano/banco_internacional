package com.bank.account.domain.model;

import com.bank.account.domain.exception.AccountNotActiveException;
import com.bank.account.domain.exception.InsufficientFundsException;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(
                1L,
                "ACC-001",
                100L,
                AccountType.SAVINGS,
                new BigDecimal("100.00"),
                AccountStatus.ACTIVE,
                CurrencyCode.USD
        );
    }


    @Test
    void shouldDepositSuccessfully() {
        account.deposit(new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), account.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenDepositIsZero() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> account.deposit(BigDecimal.ZERO));

        assertEquals("Amount must be greater than zero", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDepositIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(new BigDecimal("-10")));
    }

    @Test
    void shouldThrowExceptionWhenDepositIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> account.deposit(null));
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNotActiveOnDeposit() {
        Account inactiveAccount = new Account(
                2L,
                "ACC-002",
                200L,
                AccountType.CHECKING,
                new BigDecimal("100.00"),
                AccountStatus.BLOCKED,
                CurrencyCode.USD
        );

        assertThrows(AccountNotActiveException.class,
                () -> inactiveAccount.deposit(new BigDecimal("10")));
    }


    @Test
    void shouldWithdrawSuccessfully() {
        account.withdraw(new BigDecimal("40.00"));

        assertEquals(new BigDecimal("60.00"), account.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        assertThrows(InsufficientFundsException.class,
                () -> account.withdraw(new BigDecimal("200.00")));
    }

    @Test
    void shouldThrowExceptionWhenWithdrawIsZero() {
        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenWithdrawIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> account.withdraw(new BigDecimal("-5")));
    }

    @Test
    void shouldThrowExceptionWhenAccountIsNotActiveOnWithdraw() {
        Account inactiveAccount = new Account(
                3L,
                "ACC-003",
                300L,
                AccountType.SAVINGS,
                new BigDecimal("100.00"),
                AccountStatus.BLOCKED,
                CurrencyCode.USD
        );

        assertThrows(AccountNotActiveException.class,
                () -> inactiveAccount.withdraw(new BigDecimal("10")));
    }

    @Test
    void shouldReturnTrueWhenAccountIsActive() {
        assertTrue(account.isActive());
    }

    @Test
    void shouldReturnFalseWhenAccountIsInactive() {
        Account inactiveAccount = new Account(
                4L,
                "ACC-004",
                400L,
                AccountType.SAVINGS,
                BigDecimal.TEN,
                AccountStatus.BLOCKED,
                CurrencyCode.USD
        );

        assertFalse(inactiveAccount.isActive());
    }
}