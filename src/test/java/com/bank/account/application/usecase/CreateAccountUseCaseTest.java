package com.bank.account.application.usecase;

import com.bank.account.application.port.out.AccountRepository;
import com.bank.account.application.port.out.ClientPort;
import com.bank.account.domain.exception.InvalidAccountTypeException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientPort clientPort;

    @InjectMocks
    private CreateAccountUseCase useCase;


    @Test
    void shouldCreateAccountSuccessfully() {
        Long clientId = 100L;
        String type = "SAVINGS";

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Account result = useCase.execute(clientId, type);

        verify(clientPort, times(1)).validateClientExists(clientId);

        verify(accountRepository, times(1)).save(accountCaptor.capture());

        Account savedAccount = accountCaptor.getValue();

        assertEquals(clientId, savedAccount.getClientId());
        assertEquals(AccountType.SAVINGS, savedAccount.getType());
        assertEquals(BigDecimal.ZERO, savedAccount.getBalance());
        assertEquals(AccountStatus.ACTIVE, savedAccount.getStatus());
        assertEquals(CurrencyCode.USD, savedAccount.getCurrency());

        assertNotNull(savedAccount.getAccountNumber());
        assertEquals(savedAccount, result);
    }

    @Test
    void shouldThrowExceptionWhenAccountTypeIsInvalid() {
        Long clientId = 100L;
        String invalidType = "INVALID_TYPE";

        doNothing().when(clientPort).validateClientExists(clientId);

        assertThrows(InvalidAccountTypeException.class,
                () -> useCase.execute(clientId, invalidType));

        verify(accountRepository, never()).save(any());
    }


    @Test
    void shouldCallClientValidationBeforeAccountCreation() {
        Long clientId = 999L;
        String type = "CHECKING";

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(clientId, type);

        verify(clientPort, times(1)).validateClientExists(clientId);
    }
}