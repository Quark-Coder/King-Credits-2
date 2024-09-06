package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.SkinsForSale;
import com.kingleaks.king_credits.repository.SkinsForSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkinsForSaleService {
    private final SkinsForSaleRepository skinsForSaleRepository;

    public void savePictureSkinForSale(byte[] skinPhoto){
        SkinsForSale skinsForSale = new SkinsForSale();
        skinsForSale.setSkinPhoto(skinPhoto);
        skinsForSaleRepository.save(skinsForSale);
    }
}
