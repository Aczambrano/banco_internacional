package com.bank.account.application.port.in;

import com.bank.account.domain.model.Transaction;
import java.util.List;

public interface GetTransactionHistoryPort {
    List<Transaction> execute(Long accountId, int page, int size);
}