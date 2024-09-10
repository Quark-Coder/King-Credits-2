package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.TelegramUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PageCallback implements CallbackQueryHandler{
    private final BotService botService;
    private final TelegramUsersService telegramUsersService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("PAGE")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        deleteMessage(callbackQuery);
        Integer page;
        String callbackData = callbackQuery.getData();
        // Извлекаем номер страницы из callback_data
        page = Integer.parseInt(callbackData.split("__")[1]);

        SendMessage result = new SendMessage();
        result.setChatId(callbackQuery.getMessage().getChatId());
        String list = telegramUsersService.getAllUsersWithPagination(page);

        if (list != null) {
            InlineKeyboardButton selectCreditsRub = new InlineKeyboardButton();
            selectCreditsRub.setText("Выбрать айди");
            selectCreditsRub.setCallbackData("SELECT_ID_USER");

            // Создаем кнопки "Назад" и "Вперед"
            InlineKeyboardButton previousPageButton = new InlineKeyboardButton();
            previousPageButton.setText("⬅️ Назад");
            previousPageButton.setCallbackData("PAGE__" + (page - 1)); // Предыдущая страница

            InlineKeyboardButton nextPageButton = new InlineKeyboardButton();
            nextPageButton.setText("➡️ Вперед");
            nextPageButton.setCallbackData("PAGE__" + (page + 1)); // Следующая страница

            // Список кнопок (если это не первая страница, добавляем кнопку "Назад")
            List<InlineKeyboardButton> buttons = new ArrayList<>();
            if (page > 1) {
                buttons.add(previousPageButton);
            }
            buttons.add(nextPageButton);
            buttons.add(selectCreditsRub);

            // Настраиваем клавиатуру с кнопками
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

    private void deleteMessage(CallbackQuery callbackQuery) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(callbackQuery.getMessage().getChatId());
        deleteMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        botService.deleteMessage(deleteMessage);
    }
}
