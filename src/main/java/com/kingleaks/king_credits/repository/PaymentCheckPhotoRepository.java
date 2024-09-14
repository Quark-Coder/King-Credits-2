package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.enums.PaymentCheckPhotoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentCheckPhotoRepository extends JpaRepository<PaymentCheckPhoto, Long> {
    @Query(value = "SELECT * FROM payment_check_photo p " +
            "where p.status = 'PRICED' and p.id = :id", nativeQuery = true)
    Optional<PaymentCheckPhoto> findByIdWithStatusPriced(@Param("id") Long id);

    Optional<PaymentCheckPhoto> findByTelegramUserIdAndStatus(Long id, PaymentCheckPhotoStatus status);

    @Query(value = "SELECT * FROM payment_check_photo p " +
            "where p.status = 'PRICED' ORDER BY p.created_at DESC", nativeQuery = true)
    List<PaymentCheckPhoto> getAllPaymentCheckPhotoByStatusPriced();

    @Query(value = "SELECT COALESCE(SUM(p.price), 0) FROM payment_check_photo p " +
            "WHERE p.status = 'CONFIRMED' AND p.telegram_user_id = :telegramUserId ", nativeQuery = true)
    int countAmountPaymentCheckPhotoByCONFIRMED(@Param("telegramUserId") Long telegramUserId);

    @Query(value = "SELECT COALESCE(SUM(p.price), 0) FROM payment_check_photo p " +
            "WHERE p.status = 'CONFIRMED' AND p.created_at >= NOW() - INTERVAL '1 day' * :period", nativeQuery = true)
    int countAmountPaymentCheckPhotoByCONFIRMEDForPeriod(@Param("period") int period);

    @Query(value = "SELECT COALESCE(SUM(p.price), 0) FROM payment_check_photo p " +
            "WHERE p.status = 'CONFIRMED'", nativeQuery = true)
    int countAmountPaymentCheckPhotoByCONFIRMEDForAllTime();
}
