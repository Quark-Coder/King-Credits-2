package com.kingleaks.king_credits.bot.state;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.domain.enums.UserStatus;
import com.kingleaks.king_credits.repository.StateImageRepository;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class HomeCommand implements Command {
    private String stateName = "Главная страница";
    private final StateImageRepository stateImageRepository;
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        UserStatus userStatus = telegramUsersService.getStatus(update.getMessage().getFrom().getId()); // Получаем пользователя из базы данных
        if (!stateImageRepository.isStateImageHasPictureByName(stateName)){
            byte[] photoData = stateImageRepository.findByNameState(stateName).getPhotoData();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(photoData);
            InputFile inputFile = new InputFile(inputStream, "photo.jpg");
            SendPhoto returnPhoto = new SendPhoto();
            returnPhoto.setChatId(chatId.toString());
            returnPhoto.setPhoto(inputFile);
            botService.sendPhoto(returnPhoto);
        }

        SendMessage message = SendMessage
                .builder()
                .chatId(chatId.toString())
                .text("\uD83E\uDEE8 Привет, друг! Если ты хочешь приобрести кредиты в Critical Ops, ты попал в нужное место. \n" +
                        "\n" +
                        "Dream Shop - это лучший магазин по продаже игровой валюты в Critical Ops! \uD83D\uDE09\n" +
                        "\n" +
                        "\uD83D\uDCCA Курс – 0.2₽ = 1 CRDT \n")
                .build();

        if (userStatus.name().equals("ADMIN")) {
            message.setReplyMarkup(getAdminKeyboard());
        } else if (userStatus.name().equals("BANNED")) {
            message.setText("Вас забанили, вы можете пойти нахуй");
            message.setReplyMarkup(getBannedKeyboard());
        } else {
            message.setReplyMarkup(getUserKeyboard());
        }

        botService.sendMessage(message);
    }

    private ReplyKeyboardMarkup getBannedKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("Пойти нахуй")
        );
        keyboard.add(new KeyboardRow(btn1));
        return createReplyKeyboardMarkup(keyboard);
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
                new KeyboardButton("Загрузить картинку дропа"),
                new KeyboardButton("Загрузить картинку кейса")
        );
        List<KeyboardButton> btn5 = List.of(
                new KeyboardButton("Загрузить картинку раздела"),
                new KeyboardButton("Обновить реквизиты оплаты")
        );

        keyboard.add(new KeyboardRow(btn1));
        keyboard.add(new KeyboardRow(btn2));
        keyboard.add(new KeyboardRow(btn3));
        keyboard.add(new KeyboardRow(btn4));
        keyboard.add(new KeyboardRow(btn5));

        return createReplyKeyboardMarkup(keyboard);
    }

    private ReplyKeyboardMarkup getUserKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();

        List<KeyboardButton> btn1 = List.of(
                new KeyboardButton("\uD83D\uDCB3 Пополнить баланс"),
                new KeyboardButton("\uD83D\uDC64 Профиль")
        );
        List<KeyboardButton> btn2 = List.of(
                new KeyboardButton("\uD83D\uDD04 Вывод кредитов"),
                new KeyboardButton("\uD83C\uDFB0 Кейсы и игры")
        );
        List<KeyboardButton> btn3 = List.of(
                new KeyboardButton("\uD83D\uDCC8 Актуальный курс"),
                new KeyboardButton("\uD83D\uDC68\u200D\uD83D\uDCBB Промокод")
        );
        List<KeyboardButton> btn4 = List.of(
                new KeyboardButton("\uD83E\uDD47 Таблица лидеров"),
                new KeyboardButton("\uD83D\uDCDA Посчитать")
        );
        List<KeyboardButton> btn5 = List.of(
                new KeyboardButton("\uD83D\uDC68\u200D\uD83D\uDCBB Поддержка"),
                new KeyboardButton("\uD83D\uDCB0 Продать кредиты")
        );
        List<KeyboardButton> btn6 = List.of(
                new KeyboardButton("\uD83D\uDCC2 Отзывы")
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
