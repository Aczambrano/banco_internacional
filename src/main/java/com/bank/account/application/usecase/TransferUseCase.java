package com.bank.account.application.usecase;


import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.domain.exception.AccountNotFoundException;
import com.bank.account.domain.model.Account;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.Transfer;
import com.bank.account.domain.model.enums.TransactionType;
import com.bank.account.domain.repository.AccountRepository;
import com.bank.account.domain.repository.TransactionRepository;
import com.bank.account.domain.repository.TransferRepository;
import com.bank.account.domain.service.AccountDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
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
    @Transactional
    public Transfer execute(Long sourceId, Long targetId, BigDecimal amount) {

        Account source = accountRepository.findByIdForUpdate(sourceId)
                .orElseThrow(() -> new AccountNotFoundException(sourceId));

        Account target = accountRepository.findByIdForUpdate(targetId)
                .orElseThrow(() -> new AccountNotFoundException(targetId));

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
        return transferRepository.save(transfer);
    }
}
