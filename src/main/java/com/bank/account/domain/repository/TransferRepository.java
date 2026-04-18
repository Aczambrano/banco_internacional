package com.bank.account.domain.repository;

import com.bank.account.application.dto.response.TransactionResponse;
import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.model.Transfer;

public interface TransferRepository {

    Transfer save(Transfer transfer);
}
