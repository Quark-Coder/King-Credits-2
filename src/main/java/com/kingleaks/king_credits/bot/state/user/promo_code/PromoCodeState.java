package com.kingleaks.king_credits.bot.state.user.promo_code;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.StateImageRepository;
import com.kingleaks.king_credits.service.StateManagerService;
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
public class PromoCodeState implements Command {
    private String stateName = "Промокод";
    private final StateImageRepository stateImageRepository;
    private final BotService botService;
    private final StateManagerService stateManager;

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
            botService.sendPhoto(returnPhoto);
        }

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("\uD83D\uDC68\u200D\uD83D\uDCBB Промокод" +
                        "\nОтправьте боту промокод, чтобы получить вознаграждение.\n")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(update.getMessage().getFrom().getId());
        userState.setStatus("WAITING_FOR_ENTER_PROMOCODE");
        stateManager.setUserState(update.getMessage().getFrom().getId(), userState);

        botService.sendMessage(message);
    }
}
