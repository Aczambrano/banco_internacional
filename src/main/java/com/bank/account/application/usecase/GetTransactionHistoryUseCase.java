package com.bank.account.application.usecase;

import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.port.output.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionHistoryUseCase {

    private final TransactionRepository transactionRepository;
    private final GetAccountUseCase  getAccountUseCase;
    public GetTransactionHistoryUseCase(TransactionRepository transactionRepository, GetAccountUseCase getAccountUseCase) {
        this.transactionRepository = transactionRepository;
        this.getAccountUseCase = getAccountUseCase;
    }

    public List<Transaction> execute(Long accountId, int page, int size) {
        getAccountUseCase.execute(accountId);
        return transactionRepository.findByAccountId(accountId, page, size);
    }
}