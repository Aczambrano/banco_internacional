package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.repository.TransactionRepository;
import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import com.bank.account.infrastructure.persistence.mapper.TransactionPersistenceMapper;
import com.bank.account.infrastructure.persistence.repository.JpaTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final JpaTransactionRepository jpa;
    private final TransactionPersistenceMapper mapper;

    @Override
    public void save(Transaction tx) {
        jpa.save(toEntity(tx));
    }

    @Override
    public boolean existsByReference(String reference) {
        return jpa.existsByReference(reference);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId, int page, int size) {

        return jpa.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Transaction toDomain(TransactionEntity e) {
        Transaction tx = new Transaction(
                e.getId(),
                e.getAccountId(),
                e.getAmount(),
                e.getType()
        );

        // ⚠️ sincronizar estado y metadata
        if (e.getStatus() != null) {
            switch (e.getStatus()) {
                case SUCCESS -> tx.markAsSuccess();
                case FAILED -> tx.markAsFailed();
                case REVERSED -> tx.markAsReversed();
                default -> {}
            }
        }

        return tx;
    }

    private TransactionEntity toEntity(Transaction tx) {
        TransactionEntity e = new TransactionEntity();
        e.setId(tx.getId());
        e.setAccountId(tx.getAccountId());
        e.setAmount(tx.getAmount());
        e.setType(tx.getType());
        e.setStatus(tx.getStatus());
        e.setReference(tx.getReference());
        e.setCreatedAt(tx.getCreatedAt());
        return e;
    }
}