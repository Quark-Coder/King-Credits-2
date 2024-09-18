package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    @Query(value = "SELECT CASE WHEN p.code = :code " +
            "THEN TRUE ELSE FALSE END " +
            "FROM promo_code p WHERE p.code = :code ", nativeQuery = true)
    Boolean existsByCode(@Param("code") String code);

    @Query(value = "SELECT CASE WHEN p.status = 'EXPIRED' " +
            " THEN TRUE ELSE FALSE END " +
            " FROM promo_code p WHERE p.code = :code", nativeQuery = true)
    Boolean isExpiredDate(@Param("code") String code);

    PromoCode findByCode(String code);
}
