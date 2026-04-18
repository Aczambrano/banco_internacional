package com.bank.account.domain.port.output;

import com.bank.account.domain.model.Transfer;

public interface TransferRepository {

    Transfer save(Transfer transfer);
}
