package com.kingleaks.king_credits.bot.state.user.calculate;

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
public class CalculateState implements Command {
    private String stateName = "Посчитать";
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
            returnPhoto.setCaption("\uD83D\uDCDA Посчитать");
            returnPhoto.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("\uD83D\uDCDA Посчитать")
                    .build();
            message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendMessage(message);
        }

        SendMessage calculate = new SendMessage();
        calculate.setChatId(chatId);
        calculate.setText("Что вы хотите посчитать?");

        InlineKeyboardButton selectCreditsRub = new InlineKeyboardButton();
        selectCreditsRub.setText("Кредиты в рублях");
        selectCreditsRub.setCallbackData("CREDITS_IN_RUB");
        InlineKeyboardButton selectRubCredits = new InlineKeyboardButton();
        selectRubCredits.setText("Рубли в кредитах");
        selectRubCredits.setCallbackData("RUB_IN_CREDITS");

        List<InlineKeyboardButton> buttons = List.of(selectCreditsRub, selectRubCredits);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        calculate.setReplyMarkup(markup);
        botService.sendMessage(calculate);
    }
}
