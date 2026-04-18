package com.bank.account.application.port.out;

import com.bank.account.domain.model.Transfer;

public interface TransferRepository {

    Transfer save(Transfer transfer);
}
