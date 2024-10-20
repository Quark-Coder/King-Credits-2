package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.PaymentDetails;
import com.kingleaks.king_credits.repository.PaymentDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentDetailsService {
    private final PaymentDetailsRepository paymentDetailsRepository;

    public void save(String bankDetails, String cardNumber, String otherDetails) {
        Optional<PaymentDetails> optionalPaymentDetails = paymentDetailsRepository.findById(1L);
        if (optionalPaymentDetails.isPresent()) {
            PaymentDetails paymentDetails = optionalPaymentDetails.get();
            paymentDetails.setNameBankAndUser(bankDetails);
            paymentDetails.setCardNumber(cardNumber);
            paymentDetails.setOtherPaymentDetails(otherDetails);
            paymentDetailsRepository.save(paymentDetails);
        }
    }
}
