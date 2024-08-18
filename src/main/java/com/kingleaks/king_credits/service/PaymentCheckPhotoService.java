package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.enums.PaymentCheckPhotoStatus;
import com.kingleaks.king_credits.repository.PaymentCheckPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentCheckPhotoService {
    private final PaymentCheckPhotoRepository paymentCheckPhotoRepository;

    public void createPaymentCheckPhoto(Long telegramUserId, double amount){
        PaymentCheckPhoto paymentCheckPhoto = new PaymentCheckPhoto();

        paymentCheckPhoto.setTelegramUserId(telegramUserId);
        paymentCheckPhoto.setPrice(amount);
        paymentCheckPhoto.setCreatedAt(LocalDateTime.now());
        paymentCheckPhoto.setStatus(PaymentCheckPhotoStatus.CREATED);
        paymentCheckPhotoRepository.save(paymentCheckPhoto);
    }

    public PaymentCheckPhoto savePhoto(byte[] photoData, Long telegramUserId) {
        Optional<PaymentCheckPhoto> paymentCheckPhoto = paymentCheckPhotoRepository
                .findByTelegramUserIdAndStatus(telegramUserId, PaymentCheckPhotoStatus.CREATED);

        if (paymentCheckPhoto.isPresent()){
            PaymentCheckPhoto photoEntity = paymentCheckPhoto.get();
            photoEntity.setPhotoData(photoData);
            photoEntity.setStatus(PaymentCheckPhotoStatus.PRICED);

            return paymentCheckPhotoRepository.save(photoEntity);
        }

        return null;
    }

    public byte[] getPhotoById(Long id) {
        PaymentCheckPhoto photoEntity = paymentCheckPhotoRepository.findById(id).orElse(null);
        return photoEntity != null ? photoEntity.getPhotoData() : null;
    }
}
