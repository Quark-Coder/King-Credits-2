package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.Cases;
import com.kingleaks.king_credits.domain.entity.CasesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasesRepository extends JpaRepository<Cases, Long> {

    @Query(value = "SELECT c.* FROM cases c " +
            "WHERE c.photo_data IS NULL ", nativeQuery = true)
    List<Cases> findAllCasesWithoutPicture();
}
