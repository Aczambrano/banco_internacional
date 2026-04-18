package com.bank.account.application.port.in;

import com.bank.account.domain.model.Account;

public interface CreateAccountPort {
    Account execute(Long clientId, String type);
}