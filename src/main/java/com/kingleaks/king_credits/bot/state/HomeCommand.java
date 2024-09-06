package com.kingleaks.king_credits.bot.state;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class HomeCommand implements Command {
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        UserStatus userStatus = telegramUsersService.getStatus(update.getMessage().getFrom().getId()); // Получаем пользователя из базы данных

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId.toString())
                .text("Это главная страница")
                .build();

        if (userStatus.name().equals("ADMIN")) {
            message.setReplyMarkup(getAdminKeyboard());
        } else {
            message.setReplyMarkup(getUserKeyboard());
        }

        botService.sendMessage(message);
    }

    private ReplyKeyboardMarkup getAdminKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("Запросы на пополнение"),
                new KeyboardButton("Запросы на вывод")
        );
        List<KeyboardButton> btn2 = List.of(
                new KeyboardButton("Все пользователи"),
                new KeyboardButton("Создать промокод")
        );
        List<KeyboardButton> btn3 = List.of(
                new KeyboardButton("Черный список"),
                new KeyboardButton("Статистика")
        );
        List<KeyboardButton> btn4 = List.of(
                new KeyboardButton("Загрузить картинку дропа")
        );

        keyboard.add(new KeyboardRow(btn1));
        keyboard.add(new KeyboardRow(btn2));
        keyboard.add(new KeyboardRow(btn3));
        keyboard.add(new KeyboardRow(btn4));

        return createReplyKeyboardMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getUserKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("Пополнить баланс"),
                new KeyboardButton("Профиль")
        );
        List<KeyboardButton> btn2 = List.of(
                new KeyboardButton("Вывод кредитов"),
                new KeyboardButton("Купить скины")
        );
        List<KeyboardButton> btn3 = List.of(
                new KeyboardButton("Кейсы и игры"),
                new KeyboardButton("Актуальный курс")
        );
        List<KeyboardButton> btn4 = List.of(
                new KeyboardButton("Промокод"),
                new KeyboardButton("Таблица лидеров")
        );
        List<KeyboardButton> btn5 = List.of(
                new KeyboardButton("Посчитать"),
                new KeyboardButton("Помощь")
        );
        List<KeyboardButton> btn6 = List.of(
                new KeyboardButton("Продать кредиты"),
                new KeyboardButton("Отзывы")
        );

        keyboard.add(new KeyboardRow(btn1));
        keyboard.add(new KeyboardRow(btn2));
        keyboard.add(new KeyboardRow(btn3));
        keyboard.add(new KeyboardRow(btn4));
        keyboard.add(new KeyboardRow(btn5));
        keyboard.add(new KeyboardRow(btn6));

        return createReplyKeyboardMarkup(keyboard);
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> keyboard) {
        return ReplyKeyboardMarkup
                .builder()
                .keyboard(keyboard)
                .resizeKeyboard(true)
                .oneTimeKeyboard(false)
                .build();
    }
}
