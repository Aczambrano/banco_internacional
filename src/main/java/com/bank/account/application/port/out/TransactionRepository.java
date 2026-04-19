package com.bank.account.application.port.out;

import com.bank.account.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {

    Transaction save(Transaction transaction);

    List<Transaction> findByReference(String reference);
    List<Transaction> findByAccountId(Long accountId, int page, int size);
}
