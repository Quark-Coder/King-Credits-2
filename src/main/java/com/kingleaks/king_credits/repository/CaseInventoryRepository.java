package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.CaseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseInventoryRepository extends JpaRepository<CaseInventory, Long> {
}
