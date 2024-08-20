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
    Optional<PaymentCheckPhoto> findByTelegramUserIdAndStatus(Long id, PaymentCheckPhotoStatus status);

    @Query(value = "SELECT * FROM payment_check_photo p " +
            "where p.status = 'PRICED' ORDER BY p.created_at DESC", nativeQuery = true)
    List<PaymentCheckPhoto> getAllPaymentCheckPhotoByStatusPriced();
}
