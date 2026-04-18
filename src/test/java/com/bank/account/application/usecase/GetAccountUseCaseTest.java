package com.bank.account.application.usecase;

import com.bank.account.application.port.out.AccountRepository;
import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private GetAccountUseCase useCase;

    @Test
    void shouldReturnAccountWhenExists() {
        Long accountId = 1L;

        Account account = activeAccount();

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.of(account));

        Account result = useCase.execute(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getId());

        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {
        Long accountId = 99L;

        when(accountRepository.findById(accountId))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> useCase.execute(accountId));

        verify(accountRepository, times(1)).findById(accountId);
    }

    private Account activeAccount() {
        return new Account(
                1L,
                "ACC-001",
                100L,
                AccountType.SAVINGS,
                new BigDecimal("500.00"),
                AccountStatus.ACTIVE,
                CurrencyCode.USD
        );
    }
}