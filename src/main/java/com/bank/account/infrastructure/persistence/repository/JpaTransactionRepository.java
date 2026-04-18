package com.bank.account.infrastructure.persistence.repository;

import com.bank.account.infrastructure.persistence.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, Long> {

    boolean existsByReference(String reference);

    Page<TransactionEntity> findByAccountId(Long accountId, Pageable pageable);}