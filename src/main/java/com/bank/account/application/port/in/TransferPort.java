package com.bank.account.application.port.in;

import com.bank.account.domain.model.Transfer;
import java.math.BigDecimal;

public interface TransferPort {
    Transfer execute(Long sourceId, Long targetId, BigDecimal amount, String endToEndId);
}