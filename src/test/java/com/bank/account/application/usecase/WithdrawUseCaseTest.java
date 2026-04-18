package com.bank.account.application.usecase;

import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.exception.InsufficientFundsException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.application.port.out.AccountRepository;
import com.bank.account.application.port.out.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WithdrawUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawUseCase useCase;

    @Test
    void shouldWithdrawSuccessfully() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("50.00");

        Account account = activeAccount("200.00");

        when(accountRepository.findByIdForUpdate(accountId))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transaction result = useCase.execute(accountId, amount);

        verify(accountRepository).findByIdForUpdate(accountId);

        assertEquals(new BigDecimal("150.00"), account.getBalance());

        verify(accountRepository).save(account);
        verify(transactionRepository).save(txCaptor.capture());

        Transaction tx = txCaptor.getValue();

        assertEquals(accountId, tx.getAccountId());
        assertEquals(amount, tx.getAmount());
        assertEquals(TransactionType.WITHDRAWAL, tx.getType());

        assertNotNull(tx.getReference());
        assertNotNull(tx.getCreatedAt());

        assertEquals(result, tx);
    }


    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        Long accountId = 99L;

        when(accountRepository.findByIdForUpdate(accountId))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(accountId, new BigDecimal("50.00")));

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        Long accountId = 1L;

        Account account = activeAccount("30.00");

        when(accountRepository.findByIdForUpdate(accountId))
                .thenReturn(Optional.of(account));

        assertThrows(InsufficientFundsException.class,
                () -> useCase.execute(accountId, new BigDecimal("100.00")));

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    private Account activeAccount(String balance) {
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
}