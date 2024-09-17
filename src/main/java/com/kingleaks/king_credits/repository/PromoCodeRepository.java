package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    Boolean existsByCode(String code);
}
