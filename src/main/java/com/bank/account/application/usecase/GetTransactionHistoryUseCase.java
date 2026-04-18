package com.bank.account.application.usecase;

import com.bank.account.application.port.in.GetTransactionHistoryPort;
import com.bank.account.domain.model.Transaction;
import com.bank.account.application.port.out.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTransactionHistoryUseCase implements GetTransactionHistoryPort {

    private final TransactionRepository transactionRepository;
    private final GetAccountUseCase  getAccountUseCase;
    public GetTransactionHistoryUseCase(TransactionRepository transactionRepository, GetAccountUseCase getAccountUseCase) {
        this.transactionRepository = transactionRepository;
        this.getAccountUseCase = getAccountUseCase;
    }

    @Override
    public List<Transaction> execute(Long accountId, int page, int size) {
        getAccountUseCase.execute(accountId);
        return transactionRepository.findByAccountId(accountId, page, size);
    }
}