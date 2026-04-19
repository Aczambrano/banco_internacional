package com.bank.account.application.usecase;

import com.bank.account.application.port.out.AccountRepository;
import com.bank.account.application.port.out.TransactionRepository;
import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import com.bank.account.domain.model.enums.TransactionType;
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
class DepositUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DepositUseCase useCase;

    @Test
    void shouldDepositSuccessfully() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        String reference = "123";
        Account account = activeAccount("200.00");

        when(accountRepository.findByIdForUpdate(accountId))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transaction result = useCase.execute(accountId, amount, reference);

        verify(accountRepository).findByIdForUpdate(accountId);

        assertEquals(new BigDecimal("300.00"), account.getBalance());

        verify(accountRepository).save(account);

        verify(transactionRepository).save(txCaptor.capture());

        Transaction savedTx = txCaptor.getValue();

        assertEquals(accountId, savedTx.getAccountId());
        assertEquals(amount, savedTx.getAmount());
        assertEquals(TransactionType.DEPOSIT, savedTx.getType());
        assertEquals(result, savedTx);
        assertNotNull(savedTx.getReference());
    }


    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        Long accountId = 99L;

        when(accountRepository.findByIdForUpdate(accountId))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(accountId, new BigDecimal("50.00"),"123"));

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