package com.kingleaks.king_credits.bot.state.user.cases_and_games;

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
public class CasesAndGamesState implements Command {
    private String stateName = "Кейсы и игры";
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
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendPhoto(returnPhoto);
        } else {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text("Кейсы и игры")
                    .build();
            message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                    .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
            botService.sendMessage(message);
        }


        SendMessage selectGame = new SendMessage();
        selectGame.setChatId(chatId);
        selectGame.setText("Выберите нужную игру");

        InlineKeyboardButton headsAndTails = new InlineKeyboardButton();
        headsAndTails.setText("Орел и решка");
        headsAndTails.setCallbackData("HEADS_AND_TAILS");
        InlineKeyboardButton cases = new InlineKeyboardButton();
        cases.setText("Кейсы");
        cases.setCallbackData("CASES");

        List<InlineKeyboardButton> buttons = List.of(headsAndTails, cases);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        selectGame.setReplyMarkup(markup);
        botService.sendMessage(selectGame);
    }
}
