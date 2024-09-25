package com.kingleaks.king_credits.bot.state.admin.upload_state_image;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.StateImageService;
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
public class UploadStateImageState implements Command {
    private final BotService botService;
    private final StateImageService stateImageService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Загрузить картинку раздела")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
        botService.sendMessage(message);

        String listCases = stateImageService.getStateImageListWithoutPicAsString();

        SendMessage option = new SendMessage();
        option.setChatId(update.getMessage().getChatId());

        if (listCases != null) {
            InlineKeyboardButton selectItem = new InlineKeyboardButton();
            selectItem.setText("Выбрать раздел");
            selectItem.setCallbackData("SELECT_STATE_IMAGE");

            List<InlineKeyboardButton> buttons = List.of(selectItem);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));

            option.setText("Вот лист разделов для загрузки изображения\n" + listCases);
            option.setReplyMarkup(markup);
            botService.sendMessage(option);
        } else {
            option.setText("Нету разделов");
            botService.sendMessage(option);
        }
    }
}
