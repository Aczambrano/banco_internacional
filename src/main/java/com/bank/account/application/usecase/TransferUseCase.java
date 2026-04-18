package com.bank.account.application.usecase;


import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.Transfer;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.domain.repository.AccountRepository;
import com.bank.account.domain.repository.TransactionRepository;
import com.bank.account.domain.repository.TransferRepository;
import com.bank.account.domain.service.AccountDomainService;

import java.math.BigDecimal;

public class TransferUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransferRepository transferRepository;
    private final AccountDomainService domainService;

    public TransferUseCase(AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           TransferRepository transferRepository,
                           AccountDomainService domainService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transferRepository = transferRepository;
        this.domainService = domainService;
    }

    public void execute(Long sourceId, Long targetId, BigDecimal amount) {

        Account source = accountRepository.findByIdForUpdate(sourceId)
                .orElseThrow();

        Account target = accountRepository.findByIdForUpdate(targetId)
                .orElseThrow();

        domainService.transfer(source, target, amount);

        Transfer transfer = new Transfer(null, sourceId, targetId, amount);

        Transaction debit = new Transaction(null, sourceId, amount, TransactionType.TRANSFER_OUT);
        Transaction credit = new Transaction(null, targetId, amount, TransactionType.TRANSFER_IN);

        debit.markAsSuccess();
        credit.markAsSuccess();
        transfer.markAsSuccess();

        accountRepository.save(source);
        accountRepository.save(target);
        transactionRepository.save(debit);
        transactionRepository.save(credit);
        transferRepository.save(transfer);
    }
}
