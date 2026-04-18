package com.bank.account.domain.service;

import com.bank.account.domain.exception.AccountNotActiveException;
import com.bank.account.domain.exception.InsufficientFundsException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountDomainServiceTest {

    private AccountDomainService service;

    @BeforeEach
    void setUp() {
        service = new AccountDomainService();
    }

    @Test
    void shouldValidateDebitSuccessfully() {
        Account account = activeAccountWithBalance("100.00");

        assertDoesNotThrow(() ->
                service.validateDebit(account, new BigDecimal("50.00"))
        );
    }

    @Test
    void shouldThrowExceptionWhenDebitAccountIsNotActive() {
        Account account = inactiveAccount("100.00");

        assertThrows(AccountNotActiveException.class,
                () -> service.validateDebit(account, new BigDecimal("10.00")));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        Account account = activeAccountWithBalance("50.00");

        assertThrows(InsufficientFundsException.class,
                () -> service.validateDebit(account, new BigDecimal("100.00")));
    }

    @Test
    void shouldValidateCreditSuccessfully() {
        Account account = activeAccountWithBalance("0.00");

        assertDoesNotThrow(() ->
                service.validateCredit(account)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreditAccountIsNotActive() {
        Account account = inactiveAccount("0.00");

        assertThrows(AccountNotActiveException.class,
                () -> service.validateCredit(account));
    }

    @Test
    void shouldTransferSuccessfully() {
        Account source = activeAccountWithBalance("200.00");
        Account target = activeAccountWithBalance("50.00");

        service.transfer(source, target, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), source.getBalance());
        assertEquals(new BigDecimal("150.00"), target.getBalance());
    }

    @Test
    void shouldThrowExceptionWhenSourceAccountInactive() {
        Account source = inactiveAccount("200.00");
        Account target = activeAccountWithBalance("50.00");

        assertThrows(AccountNotActiveException.class,
                () -> service.transfer(source, target, new BigDecimal("50.00")));
    }

    @Test
    void shouldThrowExceptionWhenTargetAccountInactive() {
        Account source = activeAccountWithBalance("200.00");
        Account target = inactiveAccount("50.00");

        assertThrows(AccountNotActiveException.class,
                () -> service.transfer(source, target, new BigDecimal("50.00")));
    }

    @Test
    void shouldThrowExceptionWhenSourceHasInsufficientFunds() {
        Account source = activeAccountWithBalance("30.00");
        Account target = activeAccountWithBalance("50.00");

        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(source, target, new BigDecimal("100.00")));
    }

    private Account activeAccountWithBalance(String balance) {
        return new Account(
                1L,
                "ACC-001",
                100L,
                AccountType.SAVINGS,
                new BigDecimal(balance),
                AccountStatus.ACTIVE,
                CurrencyCode.USD
        );
    }

    private Account inactiveAccount(String balance) {
        return new Account(
                2L,
                "ACC-002",
                200L,
                AccountType.CHECKING,
                new BigDecimal(balance),
                AccountStatus.BLOCKED,
                CurrencyCode.USD
        );
    }
}