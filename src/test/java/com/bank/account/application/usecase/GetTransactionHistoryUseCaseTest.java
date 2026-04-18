package com.bank.account.application.usecase;

import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.enums.TransactionStatus;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.application.port.out.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTransactionHistoryUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private GetAccountUseCase getAccountUseCase;

    @InjectMocks
    private GetTransactionHistoryUseCase useCase;

    @Test
    void shouldReturnTransactionHistorySuccessfully() {
        Long accountId = 1L;
        int page = 0;
        int size = 10;

        List<Transaction> transactions = List.of(
                createTransaction(1L),
                createTransaction(2L)
        );

        when(getAccountUseCase.execute(accountId))
                .thenReturn(null);
        when(transactionRepository.findByAccountId(accountId, page, size))
                .thenReturn(transactions);

        List<Transaction> result = useCase.execute(accountId, page, size);

        assertEquals(2, result.size());
        assertEquals(transactions, result);

        verify(getAccountUseCase, times(1)).execute(accountId);
        verify(transactionRepository, times(1))
                .findByAccountId(accountId, page, size);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        Long accountId = 99L;

        doThrow(new AccountNotFoundException(accountId))
                .when(getAccountUseCase).execute(accountId);

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(accountId, 0, 10));

        verify(getAccountUseCase, times(1)).execute(accountId);
        verify(transactionRepository, never()).findByAccountId(anyLong(), anyInt(), anyInt());
    }

    private Transaction createTransaction(Long id) {
        return new Transaction(
                id,
                1L,
                new BigDecimal("100.00"),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCESS,
                "REF-" + id,
                LocalDateTime.now()
        );
    }
}