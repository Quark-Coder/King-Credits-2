package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.repository.PaymentCheckPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentCheckPhotoService {
    private final PaymentCheckPhotoRepository paymentCheckPhotoRepository;

    public byte[] savePhoto(byte[] photoData, Long telegramUserId) {
        PaymentCheckPhoto photoEntity = new PaymentCheckPhoto();
        photoEntity.setTelegramUserId(telegramUserId);
        photoEntity.setPhotoData(photoData);
        photoEntity.setCreatedAt(LocalDateTime.now());
        PaymentCheckPhoto a = paymentCheckPhotoRepository.save(photoEntity);
        return getPhotoById(a.getId());
    }

    public byte[] getPhotoById(Long id) {
        PaymentCheckPhoto photoEntity = paymentCheckPhotoRepository.findById(id).orElse(null);
        return photoEntity != null ? photoEntity.getPhotoData() : null;
    }
}
