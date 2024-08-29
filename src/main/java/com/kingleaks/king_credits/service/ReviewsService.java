package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Reviews;
import com.kingleaks.king_credits.domain.enums.ReviewsStatus;
import com.kingleaks.king_credits.repository.ReviewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewsService {
    private final ReviewsRepository reviewsRepository;

    public Reviews findByPhotoId(Long photoId) {
        return reviewsRepository.findByPaymentCheckPhotoId(photoId);
    }

    public Reviews findByTelegramUserId(Long telegramUserId) {
        return reviewsRepository.findByTelegramUserIdAndStatus(telegramUserId);
    }

    public void createReview(Long telegramUserId, Long photoId) {
        Reviews reviews = new Reviews();
        reviews.setTelegramUserId(telegramUserId);
        reviews.setPaymentCheckPhotoId(photoId);
        reviews.setCreatedAt(LocalDateTime.now());
        reviews.setStatus(ReviewsStatus.WITHOUT_COMMENT);
        reviewsRepository.save(reviews);
    }

    public void addComment(Long photoId, String comment) {
        Reviews reviews = findByPhotoId(photoId);
        reviews.setComment(comment);
        reviews.setStatus(ReviewsStatus.WITH_COMMENT);
        reviewsRepository.save(reviews);
    }
}
