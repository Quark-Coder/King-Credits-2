package com.kingleaks.king_credits.bot.state;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@Slf4j
public class HomeCommand implements Command {
    private final BotService botService;

    @Autowired
    public HomeCommand(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void execute(long chatId) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text("Это главная страница")
                .build();

        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("Пополнить баланс"),
                new KeyboardButton("Профиль")
        );
        List<KeyboardButton> btn2 = List.of(
                new KeyboardButton("Вывод кредитов"),
                new KeyboardButton("Актуальный курс")
        );
        List<KeyboardButton> btn3 = List.of(
                new KeyboardButton("Посчитать"),
                new KeyboardButton("Помощь")
        );
        List<KeyboardButton> btn4 = List.of(
                new KeyboardButton("Продать кредиты"),
                new KeyboardButton("Отзывы")
        );

        message.setReplyMarkup(ReplyKeyboardMarkup
                .builder()
                .keyboardRow(new KeyboardRow(btn1))
                .keyboardRow(new KeyboardRow(btn2))
                .keyboardRow(new KeyboardRow(btn3))
                .keyboardRow(new KeyboardRow(btn4))
                .build());

        botService.sendMessage(message);
    }
}
