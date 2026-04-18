package com.bank.account.application.mapper;

import com.bank.account.application.dto.response.TransferResponse;

public class TransferMapper {

    public static TransferResponse fromTransfer(com.bank.account.domain.model.Transfer transfer) {
        return new TransferResponse(
                transfer.getId(),
                transfer.getSourceAccountId(),
                transfer.getTargetAccountId(),
                transfer.getAmount(),
                transfer.getStatus(),
                transfer.getReference(),
                transfer.getCreatedAt()
        );
    }
}
