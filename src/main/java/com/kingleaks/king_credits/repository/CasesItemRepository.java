package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.CasesItem;
import com.kingleaks.king_credits.domain.enums.CaseName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasesItemRepository extends JpaRepository<CasesItem, Long> {
    @Query(value = "SELECT c.* FROM cases_item c " +
            "WHERE c.cases_name = :caseName ", nativeQuery = true )
    List<CasesItem> findAllCasesItemByCaseName(@Param("caseName") String caseName);
}
