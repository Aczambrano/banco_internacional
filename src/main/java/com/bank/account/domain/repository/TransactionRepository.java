package com.bank.account.domain.repository;

import com.bank.account.domain.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    boolean existsByReference(String reference);

    List<Transaction> findByAccountId(Long accountId, int page, int size);
}
