package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Transfer;
import com.bank.account.application.port.out.TransferRepository;
import com.bank.account.infrastructure.persistence.mapper.TransferPersistenceMapper;
import com.bank.account.infrastructure.persistence.repository.JpaTransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TransferRepositoryAdapter implements TransferRepository {

    private final JpaTransferRepository jpa;
    private final TransferPersistenceMapper mapper;

    @Override
    public Transfer save(Transfer transfer) {
        return mapper.toResponse(jpa.save(mapper.toEntity(transfer)));

    }

}