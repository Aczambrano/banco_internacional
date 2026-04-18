package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Transfer;
import com.bank.account.application.port.out.TransferRepository;
import com.bank.account.infrastructure.persistence.entity.TransferEntity;
import com.bank.account.infrastructure.persistence.mapper.TransferPersistenceMapper;
import com.bank.account.infrastructure.persistence.repository.JpaTransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class TransferRepositoryAdapter implements TransferRepository {

    private final JpaTransferRepository jpa;
    private final TransferPersistenceMapper mapper;

    /*
    @Override
    public Transfer save(Transfer transfer) {
        return mapper.toResponse(jpa.save(mapper.toEntity(transfer)));

    }*/
    @Override
    public Transfer save(Transfer transfer) {

        TransferEntity transferEntity = mapper.toEntity(transfer);
        System.out.println("Entidad Transfer convertida: " + transferEntity);

        // Paso 3: Guarda la entidad en la base de datos utilizando JPA (esto podría ser el repositorio)
        TransferEntity savedEntity = jpa.save(transferEntity);
        System.out.println("Entidad guardada en la base de datos: " + savedEntity);

        // Paso 4: Convierte la entidad guardada de nuevo a un DTO (TransferResponse)
        Transfer responseTransfer = mapper.toResponse(savedEntity);
        System.out.println("TransferResponse creado: " + responseTransfer);

        // Paso 5: Retorna el TransferResponse
        return responseTransfer;
    }

}