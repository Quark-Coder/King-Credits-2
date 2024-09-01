package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.Cases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasesRepository extends JpaRepository<Cases, Long> {
}
