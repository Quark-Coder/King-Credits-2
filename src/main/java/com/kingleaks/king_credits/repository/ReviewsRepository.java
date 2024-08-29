package com.kingleaks.king_credits.repository;

import com.kingleaks.king_credits.domain.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Long> {
    @Query(value = "SELECT r FROM reviews r " +
            "WHERE r.payment_check_photo_id = :photoId " +
            "AND r.status = 'WITHOUT_COMMENT' " +
            "AND r.created_at >= NOW() - INTERVAL '24 hour' ", nativeQuery = true)
    Reviews findByTelegramUserIdAndStatus(@Param("photoId") Long photoId);

    Reviews findByPaymentCheckPhotoId(Long photoId);

    @Query(value = "SELECT r.* FROM reviews r " +
            "WHERE r.status = 'WITHOUT_COMMENT' " +
            "AND r.created_at <= NOW() - INTERVAL '24 hour' ", nativeQuery = true)
    List<Reviews> findAllReviewsWithoutComment();

    @Query(value = "SELECT r.* FROM reviews r " +
            "WHERE r.status = 'WITH_COMMENT' " +
            "AND r.created_at <= NOW() - INTERVAL '24 hour' ", nativeQuery = true)
    List<Reviews> findAllReviewsWithComment();
}
