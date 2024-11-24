package com.kingleaks.king_credits.bot.state.admin.all_users;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.TelegramUsersService;
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
public class AllUsersState implements Command {
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Integer page = 1;

        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text("Все пользователи")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("\uD83D\uDCC3 Меню"))))
                .resizeKeyboard(true).build());
        botService.sendMessage(message);

        SendMessage result = new SendMessage();
        result.setChatId(chatId);

        // Получаем пользователей на текущей странице
        String list = telegramUsersService.getAllUsersWithPagination(page);

        if (list != null) {
            InlineKeyboardButton selectCreditsRub = new InlineKeyboardButton();
            selectCreditsRub.setText("Выбрать айди");
            selectCreditsRub.setCallbackData("SELECT_ID_USER");

            InlineKeyboardButton nextPageButton = new InlineKeyboardButton();
            nextPageButton.setText("➡️ Вперед");
            nextPageButton.setCallbackData("PAGE__" + (page + 1));

            List<InlineKeyboardButton> buttons = new ArrayList<>();
            buttons.add(nextPageButton);
            buttons.add(selectCreditsRub);

            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));

            result.setText("Всего пользователей (стр. " + page + "):\n\n" + list);
            result.setReplyMarkup(markup);
            botService.sendMessage(result);
        } else {
            result.setText("Нету ничего");
            botService.sendMessage(result);
        }
    }
}
