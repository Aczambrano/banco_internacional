package com.bank.account.infrastructure.persistence.repository;

import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByReference(String reference);

    List<TransactionEntity> findByAccountIdOrderByCreatedAtDesc(Long accountId);
}