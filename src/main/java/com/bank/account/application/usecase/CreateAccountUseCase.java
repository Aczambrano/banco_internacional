package com.bank.account.application.usecase;

import com.bank.account.domain.exception.InvalidAccountTypeException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.enums.AccountStatus;
import com.bank.account.domain.model.enums.AccountType;
import com.bank.account.domain.model.enums.CurrencyCode;
import com.bank.account.domain.port.output.AccountRepository;
import com.bank.account.domain.port.output.ClientPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CreateAccountUseCase {

    private final AccountRepository accountRepository;
    private final ClientPort clientPort;

    public CreateAccountUseCase(AccountRepository accountRepository, ClientPort clientPort) {
        this.accountRepository = accountRepository;
        this.clientPort = clientPort;
    }

    public Account execute(Long clientId, String type) {
        clientPort.validateClientExists(clientId);

        try {
            Account account = new Account(
                    null,
                    generateAccountNumber(),
                    clientId,
                    AccountType.valueOf(type),
                    BigDecimal.ZERO,
                    AccountStatus.ACTIVE,
                    CurrencyCode.USD
            );
            return accountRepository.save(account);

        }catch (IllegalArgumentException e) {
            throw new InvalidAccountTypeException(type);
        }

    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }
}