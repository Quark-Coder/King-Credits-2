package com.kingleaks.king_credits.bot.state.calculate;

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
public class CalculateState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Посчитать")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendMessage(message);

        SendMessage calculate = new SendMessage();
        calculate.setChatId(update.getMessage().getChatId());
        calculate.setText("Что вы хотите посчитать?");


        InlineKeyboardButton selectCreditsRub = new InlineKeyboardButton();
        selectCreditsRub.setText("Кредиты в рублях");
        selectCreditsRub.setCallbackData("CREDITS_IN_RUB");
        InlineKeyboardButton selectRubCredits = new InlineKeyboardButton();
        selectRubCredits.setText("Рубли в кредитах");
        selectRubCredits.setCallbackData("RUB_IN_CREDITS");

        List<InlineKeyboardButton> buttons = List.of(selectCreditsRub, selectRubCredits);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        calculate.setReplyMarkup(markup);
        botService.sendMessage(calculate);
    }
}
