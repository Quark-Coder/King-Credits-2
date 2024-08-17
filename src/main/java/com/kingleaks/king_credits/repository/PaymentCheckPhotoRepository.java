package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.enums.PaymentCheckPhotoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentCheckPhotoRepository extends JpaRepository<PaymentCheckPhoto, Long> {
    Optional<PaymentCheckPhoto> findByTelegramUserIdAndStatus(Long id, PaymentCheckPhotoStatus status);
}
