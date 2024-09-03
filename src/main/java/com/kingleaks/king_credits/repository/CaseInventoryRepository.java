package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.CaseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseInventoryRepository extends JpaRepository<CaseInventory, Long> {

    @Query(value = "SELECT * FROM case_inventory c " +
            "WHERE c.telegram_user_id = :telegramUserId ", nativeQuery = true)
    List<CaseInventory> findAllByTelegramUserId(@Param("telegramUserId") Long telegramUserId);
}
