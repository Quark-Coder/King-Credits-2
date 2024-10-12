package com.kingleaks.king_credits.bot.state.admin.statistics;

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

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticsState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Статистика")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню"))))
                .resizeKeyboard(true).build());
        botService.sendMessage(message);

        SendMessage result = new SendMessage();
        result.setChatId(update.getMessage().getChatId());
        InlineKeyboardButton day = new InlineKeyboardButton();
        day.setText("День");
        day.setCallbackData("PERIOD__"+1);

        InlineKeyboardButton week = new InlineKeyboardButton();
        week.setText("Неделя");
        week.setCallbackData("PERIOD__"+7);

        InlineKeyboardButton month = new InlineKeyboardButton();
        month.setText("Месяц");
        month.setCallbackData("PERIOD__"+30);

        InlineKeyboardButton allTime = new InlineKeyboardButton();
        allTime.setText("Все время");
        allTime.setCallbackData("PERIOD__ALL_TIME");

        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons2 = new ArrayList<>();
        buttons1.add(day);
        buttons1.add(week);
        buttons2.add(month);
        buttons2.add(allTime);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons1, buttons2));

        result.setText("Выберите период времени");
        result.setReplyMarkup(markup);
        botService.sendMessage(result);
    }
}
