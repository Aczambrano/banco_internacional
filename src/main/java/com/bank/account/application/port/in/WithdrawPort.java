package com.bank.account.application.port.in;

import com.bank.account.domain.model.Transaction;
import java.math.BigDecimal;

public interface WithdrawPort {
    Transaction execute(Long accountId, BigDecimal amount, String endToEndId);
}