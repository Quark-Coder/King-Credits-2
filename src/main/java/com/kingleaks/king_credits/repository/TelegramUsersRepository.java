package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramUsersRepository extends JpaRepository<TelegramUsers, Long> {
    Optional<TelegramUsers> findByUserId(Long userId);

    @Query(value = "SELECT coalesce(a.balance, 0) FROM telegram_users t " +
            "JOIN account a on t.user_id = a.telegram_user_id" +
            " where t.user_id = :userId", nativeQuery = true)
    BigDecimal getBalanceByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT t.status FROM telegram_users t " +
            "where t.user_id = :userId", nativeQuery = true)
    UserStatus getUserStatusByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT t.* FROM telegram_users t " +
            "WHERE t.status = 'ADMIN' ", nativeQuery = true)
    List<TelegramUsers> findAllAdmins();

    @Query(value = "SELECT t.* FROM telegram_users t " +
            "WHERE t.status = 'USER' ", nativeQuery = true)
    List<TelegramUsers> findAllUsers();

    @Query(value = "SELECT t.* FROM telegram_users t " +
            "JOIN account a on t.user_id = a.telegram_user_id " +
            "ORDER BY a.balance DESC ", nativeQuery = true)
    List<TelegramUsers> findAllTelegramUsersForLeaderBoard();
}
