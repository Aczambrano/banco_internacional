package com.bank.account.infrastructure.persistence.adapter;

import com.bank.account.domain.model.Transaction;
import com.bank.account.domain.port.output.TransactionRepository;
import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import com.bank.account.infrastructure.persistence.mapper.TransactionPersistenceMapper;
import com.bank.account.infrastructure.persistence.repository.JpaTransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final JpaTransactionRepository jpa;
    private final TransactionPersistenceMapper mapper;

    @Override
    public Transaction save(Transaction tx) {
        return mapper.toResponse(jpa.save(mapper.toEntity(tx)));
    }

    @Override
    public boolean existsByReference(String reference) {
        return jpa.existsByReference(reference);
    }

    @Override
    public List<Transaction> findByAccountId(Long accountId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        return jpa.findByAccountId(accountId, pageable)
                .map(this::toDomain)
                .toList();
    }

    private Transaction toDomain(TransactionEntity e) {
        Transaction tx = new Transaction(
                e.getId(),
                e.getAccountId(),
                e.getAmount(),
                e.getType()
        );

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

}