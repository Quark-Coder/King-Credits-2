package com.kingleaks.king_credits.bot.state.user.buy_skins;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.repository.StateImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BuySkinsState implements Command {
    private String stateName = "Купить скины";
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
            returnPhoto.setCaption(stateName);
            returnPhoto.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Купить скины")
                    .build();
            botService.sendMessage(message);
        }

        SendMessage option = new SendMessage();
        option.setChatId(chatId);

        InlineKeyboardButton urlStore = new InlineKeyboardButton();
        urlStore.setText("Cсылка на магазин скинов");
        urlStore.setUrl("https://youtu.be/dQw4w9WgXcQ?si=5geLgShGreJXCU-w");

        InlineKeyboardButton urlProfile = new InlineKeyboardButton();
        urlProfile.setText("Cсылка на наш профиль");
        urlProfile.setUrl("https://youtu.be/L_jWHffIx5E?si=wzPJhQFExlYSroct");

        List<InlineKeyboardButton> buttons = List.of(urlStore, urlProfile);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        option.setText("Выберите опцию");
        option.setReplyMarkup(markup);
        botService.sendMessage(option);
    }
}
