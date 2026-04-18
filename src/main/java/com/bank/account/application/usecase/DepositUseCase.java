package com.bank.account.application.usecase;

import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.domain.repository.AccountRepository;
import com.bank.account.domain.repository.TransactionRepository;

public class DepositUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DepositUseCase(AccountRepository accountRepository,
                          TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public void execute(Long accountId, java.math.BigDecimal amount) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.deposit(amount);

        Transaction tx = new Transaction(
                null,
                accountId,
                amount,
                TransactionType.DEPOSIT
        );

        tx.markAsSuccess();

        accountRepository.save(account);
        transactionRepository.save(tx);
    }
}