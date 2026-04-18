package com.bank.account.application.usecase;


import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.domain.repository.AccountRepository;
import com.bank.account.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WithdrawUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public WithdrawUseCase(AccountRepository accountRepository,
                           TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction execute(Long accountId, BigDecimal amount) {

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.withdraw(amount);

        Transaction tx = new Transaction(
                null,
                accountId,
                amount,
                TransactionType.WITHDRAWAL
        );

        tx.markAsSuccess();

        accountRepository.save(account);
        return transactionRepository.save(tx);
    }
}