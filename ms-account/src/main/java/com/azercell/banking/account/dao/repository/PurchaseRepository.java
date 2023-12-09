package com.azercell.banking.account.dao.repository;

import com.azercell.banking.account.dao.entity.PurchaseEntity;
import com.azercell.banking.account.model.enums.PurchaseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

    Optional<PurchaseEntity> findByIdAndStatus(Long purchaseId, PurchaseStatus status);

}
