package com.kingleaks.king_credits.bot.state.user.cases_and_games;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CasesAndGamesState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Кейсы и игры")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendMessage(message);


        SendMessage selectGame = new SendMessage();
        selectGame.setChatId(update.getMessage().getChatId());
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
