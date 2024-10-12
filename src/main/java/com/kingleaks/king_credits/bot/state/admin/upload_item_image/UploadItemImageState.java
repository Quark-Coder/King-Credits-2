package com.kingleaks.king_credits.bot.state.admin.upload_item_image;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.CasesItemService;
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
public class UploadItemImageState implements Command {
    private final BotService botService;
    private final CasesItemService casesItemService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Загрузить картинку дропа")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню"))))
                .resizeKeyboard(true).build());
        botService.sendMessage(message);

        String listItems = casesItemService.getItemListAsString();

        SendMessage option = new SendMessage();
        option.setChatId(update.getMessage().getChatId());

        if (listItems != null) {
            InlineKeyboardButton selectItem = new InlineKeyboardButton();
            selectItem.setText("Выбрать предмет");
            selectItem.setCallbackData("SELECT_ITEM_WITHOUT_IMAGE");

            List<InlineKeyboardButton> buttons = List.of(selectItem);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));

            option.setText("Вот лист предметов у которых отсутствует картинке\n" + listItems);
            option.setReplyMarkup(markup);
            botService.sendMessage(option);
        } else {
            option.setText("У всех предметов есть картинка или отсутствует сами предметы");
            botService.sendMessage(option);
        }
    }
}
