package com.bank.account.application.usecase;

import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.Transfer;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.domain.service.AccountDomainService;
import com.bank.account.application.port.out.AccountRepository;
import com.bank.account.application.port.out.TransactionRepository;
import com.bank.account.application.port.out.TransferRepository;
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
class TransferUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private AccountDomainService domainService;

    @InjectMocks
    private TransferUseCase useCase;

    @Test
    void shouldTransferSuccessfully() {
        Long sourceId = 1L;
        Long targetId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        Account source = activeAccount("200.00");
        Account target = activeAccount("50.00");

        when(accountRepository.findByIdForUpdate(sourceId))
                .thenReturn(Optional.of(source));

        when(accountRepository.findByIdForUpdate(targetId))
                .thenReturn(Optional.of(target));

        when(transferRepository.save(any(Transfer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);

        Transfer result = useCase.execute(sourceId, targetId, amount);

        verify(accountRepository).findByIdForUpdate(sourceId);
        verify(accountRepository).findByIdForUpdate(targetId);

        verify(domainService).transfer(source, target, amount);

        verify(accountRepository, times(1)).save(source);
        verify(accountRepository, times(1)).save(target);

        verify(transactionRepository, times(2)).save(txCaptor.capture());

        assertEquals(2, txCaptor.getAllValues().size());

        Transaction debit = txCaptor.getAllValues().get(0);
        Transaction credit = txCaptor.getAllValues().get(1);

        assertEquals(TransactionType.TRANSFER_OUT, debit.getType());
        assertEquals(TransactionType.TRANSFER_IN, credit.getType());

        assertEquals(sourceId, debit.getAccountId());
        assertEquals(targetId, credit.getAccountId());

        assertEquals(amount, debit.getAmount());
        assertEquals(amount, credit.getAmount());

        verify(transferRepository).save(any(Transfer.class));

        assertNotNull(result);
    }

    @Test
    void shouldThrowExceptionWhenSourceAccountNotFound() {
        Long sourceId = 1L;

        when(accountRepository.findByIdForUpdate(sourceId))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(sourceId, 2L, new BigDecimal("50.00")));

        verify(domainService, never()).transfer(any(), any(), any());
        verify(transactionRepository, never()).save(any());
        verify(transferRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenTargetAccountNotFound() {
        Long sourceId = 1L;
        Long targetId = 2L;

        Account source = activeAccount("100.00");

        when(accountRepository.findByIdForUpdate(sourceId))
                .thenReturn(Optional.of(source));

        when(accountRepository.findByIdForUpdate(targetId))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(sourceId, targetId, new BigDecimal("50.00")));

        verify(domainService, never()).transfer(any(), any(), any());
        verify(transactionRepository, never()).save(any());
        verify(transferRepository, never()).save(any());
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