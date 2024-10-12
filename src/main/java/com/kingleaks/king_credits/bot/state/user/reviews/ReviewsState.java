package com.kingleaks.king_credits.bot.state.user.reviews;

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
public class ReviewsState implements Command {
    private String stateName = "Отзывы";
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
            InlineKeyboardButton urlStore = new InlineKeyboardButton();
            urlStore.setText("✅ Отзывы");
            urlStore.setUrl("https://t.me/KingCreditsReviews");
            List<InlineKeyboardButton> buttons = List.of(urlStore);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));
            returnPhoto.setCaption("\uD83D\uDC6E\u200D♂\uFE0F Удостоверьтесь в безопасности проведения сделок.\n" +
                    "\n" +
                    "Предлагаем ознакомиться с отзывами от наших клиентов. Для этого нажмите кнопку ниже.\n");
            returnPhoto.setReplyMarkup(markup);
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("\uD83D\uDC6E\u200D♂\uFE0F Удостоверьтесь в безопасности проведения сделок.\n" +
                            "\n" +
                            "Предлагаем ознакомиться с отзывами от наших клиентов. Для этого нажмите кнопку ниже.\n")
                    .build();
            InlineKeyboardButton urlStore = new InlineKeyboardButton();
            urlStore.setText("✅ Отзывы");
            urlStore.setUrl("https://t.me/KingCreditsReviews");
            List<InlineKeyboardButton> buttons = List.of(urlStore);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));
            message.setReplyMarkup(markup);

            botService.sendMessage(message);
        }
    }
}
