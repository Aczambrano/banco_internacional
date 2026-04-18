package com.bank.account.application.usecase;

import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionHistoryUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionHistoryUseCase(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> execute(Long accountId, int page, int size) {
        return transactionRepository.findByAccountId(accountId, page, size);
    }
}