package com.bank.account.application.mapper;

import com.bank.account.application.dto.response.AccountResponse;
import com.bank.account.domain.model.Account;

public class AccountMapper {

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getClientId(),
                account.getType(),
                account.getBalance(),
                account.getStatus(),
                account.getCurrency()
        );
    }
}