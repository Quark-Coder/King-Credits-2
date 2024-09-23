package com.kingleaks.king_credits.bot.state.admin.upload_cases_image;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.CasesService;
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
public class UploadCasesImageState implements Command {
    private final BotService botService;
    private final CasesService casesService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Загрузить картинку кейса")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
        botService.sendMessage(message);

        String listCases = casesService.getCasesListWithoutPicAsString();

        SendMessage option = new SendMessage();
        option.setChatId(update.getMessage().getChatId());

        if (listCases != null) {
            InlineKeyboardButton selectItem = new InlineKeyboardButton();
            selectItem.setText("Выбрать кейс");
            selectItem.setCallbackData("SELECT_CASES_WITHOUT_IMAGE");

            List<InlineKeyboardButton> buttons = List.of(selectItem);
            InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
            markup.setKeyboard(List.of(buttons));

            option.setText("Вот лист кейсов у которых отсутствует картинке\n" + listCases);
            option.setReplyMarkup(markup);
            botService.sendMessage(option);
        } else {
            option.setText("У всех кейсов есть картинка или отсутствует сами кейсы");
            botService.sendMessage(option);
        }
    }
}
