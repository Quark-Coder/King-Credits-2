package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.UsedPromoCodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedPromoCodesRepository extends JpaRepository<UsedPromoCodes, Long> {

    @Query(value = "SELECT CASE WHEN p.count_users > " +
            " COALESCE((SELECT COUNT(*) FROM used_promo_codes upc " +
            " WHERE upc.code = :code), 0) " +
            " THEN TRUE ELSE FALSE END " +
            " FROM promo_code p WHERE p.code = :code", nativeQuery = true)
    Boolean existsByCountUser(@Param("code") String code);

    @Query(value = "SELECT CASE WHEN u.telegram_user_id = :telegramUserId AND u.code = :code " +
            "THEN TRUE ELSE FALSE END " +
            "FROM used_promo_codes u WHERE u.telegram_user_id = :telegramUserId" +
            " AND u.code = :code ", nativeQuery = true)
    Boolean userHasPromoCode(@Param("telegramUserId") Long telegramUserId, @Param("code") String code);
}
