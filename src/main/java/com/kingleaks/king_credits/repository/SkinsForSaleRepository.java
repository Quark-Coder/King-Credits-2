package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.SkinsForSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkinsForSaleRepository extends JpaRepository<SkinsForSale, Long> {
}
