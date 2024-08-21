package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.Account;
import com.kingleaks.king_credits.domain.entity.PaymentCheckPhoto;
import com.kingleaks.king_credits.domain.entity.TelegramUsers;
import com.kingleaks.king_credits.domain.enums.PaymentCheckPhotoStatus;
import com.kingleaks.king_credits.repository.AccountRepository;
import com.kingleaks.king_credits.repository.PaymentCheckPhotoRepository;
import com.kingleaks.king_credits.repository.TelegramUsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReplenishmentRequestsService {
    private final PaymentCheckPhotoRepository paymentCheckPhotoRepository;
    private final AccountRepository accountRepository;
    private final TelegramUsersRepository telegramUsersRepository;

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

    public PaymentCheckPhoto selectPaymentCheckPhotoById(Long id) {
        Optional<PaymentCheckPhoto> paymentCheckPhoto = paymentCheckPhotoRepository.findByIdWithStatusPriced(id);
        return paymentCheckPhoto.orElse(null);
    }

    public TelegramUsers confirmRequest(Long id){
        Optional<PaymentCheckPhoto> paymentCheckPhoto = paymentCheckPhotoRepository.findById(id);
        if (paymentCheckPhoto.isPresent()) {
            PaymentCheckPhoto checkPhoto = paymentCheckPhoto.get();
            checkPhoto.setStatus(PaymentCheckPhotoStatus.CONFIRMED);

            Optional<Account> account = accountRepository.findByTelegramUserId(checkPhoto.getTelegramUserId());
            if (account.isPresent()) {
                Account resultAccount = account.get();
                BigDecimal balance = resultAccount.getBalance();
                BigDecimal price = BigDecimal.valueOf(checkPhoto.getPrice());
                BigDecimal result = balance.add(price);
                resultAccount.setBalance(result);
                accountRepository.save(resultAccount);
                paymentCheckPhotoRepository.save(checkPhoto);

                return telegramUsersRepository.findByUserId(checkPhoto.getTelegramUserId()).orElse(null);
            }
        }

        return null;
    }

    public TelegramUsers rejectRequest(Long id){
        Optional<PaymentCheckPhoto> paymentCheckPhoto = paymentCheckPhotoRepository.findById(id);
        if (paymentCheckPhoto.isPresent()) {
            PaymentCheckPhoto checkPhoto = paymentCheckPhoto.get();
            checkPhoto.setStatus(PaymentCheckPhotoStatus.ARCHIVED);
            paymentCheckPhotoRepository.save(checkPhoto);
            return telegramUsersRepository.findByUserId(checkPhoto.getTelegramUserId()).orElse(null);
        }
        return null;
    }
}
