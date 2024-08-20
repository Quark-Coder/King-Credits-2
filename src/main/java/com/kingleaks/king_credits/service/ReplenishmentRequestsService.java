package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.repository.PaymentCheckPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReplenishmentRequestsService {
    private final PaymentCheckPhotoRepository paymentCheckPhotoRepository;

    public String getAllListReplenishmentRequests() {
        List<PaymentCheckPhoto> checkPhotoList = paymentCheckPhotoRepository.getAllPaymentCheckPhotoByStatusPriced();
        if (checkPhotoList.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder();

        for (PaymentCheckPhoto checkPhoto : checkPhotoList) {
            Long id = checkPhoto.getId();
            Double price = checkPhoto.getPrice();
            result.append("чек - ").append(id).append(" сумма - ").append(price).append("\n");
        }

        return result.toString().trim();
    }
}
