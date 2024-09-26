package com.kingleaks.king_credits.bot.state.user.sell_credits;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.repository.StateImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SellCreditsState implements Command {
    private String stateName = "Продать кредиты";
    private final StateImageRepository stateImageRepository;
    private final BotService botService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        if (!stateImageRepository.isStateImageHasPictureByName(stateName)){
            byte[] photoData = stateImageRepository.findByNameState(stateName).getPhotoData();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            InputFile inputFile = new InputFile(inputStream, "photo.jpg");
            SendPhoto returnPhoto = new SendPhoto();
            returnPhoto.setChatId(chatId.toString());
            returnPhoto.setPhoto(inputFile);
            returnPhoto.setCaption("Если вы хотите продать кредиты, мы можем помочь с этим." +
                    " Для этого обратитесь в нашу поддержку: @DreamCredits");
            returnPhoto.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Если вы хотите продать кредиты, мы можем помочь с этим." +
                            " Для этого обратитесь в нашу поддержку: @DreamCredits")
                    .build();
            message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());

            botService.sendMessage(message);
        }
    }
}
