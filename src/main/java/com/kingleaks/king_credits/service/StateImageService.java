package com.kingleaks.king_credits.service;

import com.kingleaks.king_credits.domain.entity.StateImage;
import com.kingleaks.king_credits.repository.StateImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StateImageService {
    private final StateImageRepository stateImageRepository;


    public String getStateImageListWithoutPicAsString() {
        List<StateImage> stateList = stateImageRepository.findAllStateForPicture();
        if (stateList.isEmpty()){
            return null;
        }
        String result = "";
        for (StateImage state : stateList) {
            result = result + "№" + state.getId() + " " + state.getNameState() + "\n";
        }
        return result;
    }

    public String getInformationState(Long id){
        Optional<StateImage> OptionalStateImage = stateImageRepository.findById(id);
        if (OptionalStateImage.isPresent()){
            StateImage stateImage = OptionalStateImage.get();
            Long stateImageId = stateImage.getId();
            String nameState = stateImage.getNameState();
            Boolean hasPicture = stateImageRepository.isStateImageHasPicture(id);

            return "Айди - " + stateImageId +
                    "\nНазвание - "  + nameState +
                    "\nЕсть ли изображения - " + !hasPicture;
        }

        return null;
    }

    public void savePictureForState(Long itemId, byte[] picture){
        Optional<StateImage> optionalStateImage = stateImageRepository.findById(itemId);

        if (optionalStateImage.isPresent()){
            StateImage stateImage = optionalStateImage.get();
            stateImage.setPhotoData(picture);
            stateImageRepository.save(stateImage);
        }
    }

    public void deletePictureForState(Long stateId){
        Optional<StateImage> optionalStateImage = stateImageRepository.findById(stateId);

        if (optionalStateImage.isPresent()){
            StateImage stateImage = optionalStateImage.get();
            stateImage.setPhotoData(null);
            stateImageRepository.save(stateImage);
        }
    }
}
