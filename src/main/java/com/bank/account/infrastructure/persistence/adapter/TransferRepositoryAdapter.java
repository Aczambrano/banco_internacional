package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Transfer;
import com.bank.account.domain.repository.TransferRepository;
import com.bank.account.infrastructure.persistence.entity.TransferEntity;
import com.bank.account.infrastructure.persistence.repository.JpaTransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TransferRepositoryAdapter implements TransferRepository {

    private final JpaTransferRepository jpa;


    @Override
    public void save(Transfer transfer) {
        jpa.save(toEntity(transfer));
    }

    private TransferEntity toEntity(Transfer t) {
        TransferEntity e = new TransferEntity();
        e.setId(t.getId());
        e.setSourceAccountId(t.getSourceAccountId());
        e.setTargetAccountId(t.getTargetAccountId());
        e.setAmount(t.getAmount());
        e.setStatus(t.getStatus());
        e.setReference(t.getReference());
        e.setCreatedAt(t.getCreatedAt());
        return e;
    }
}