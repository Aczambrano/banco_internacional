package com.bank.account.infrastructure.persistence.repository;

import com.bank.account.infrastructure.persistence.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTransferRepository extends JpaRepository<TransferEntity, Long> {
}