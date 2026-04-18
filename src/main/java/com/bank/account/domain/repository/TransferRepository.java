package com.bank.account.domain.repository;

import com.bank.account.domain.model.Transfer;

public interface TransferRepository {

    void save(Transfer transfer);
}
