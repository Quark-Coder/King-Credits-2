package com.kingleaks.king_credits.bot.state.user.withdrawal_of_credits;

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
public class WithdrawalOfCreditsState implements Command {
    private final BotService botService;

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage welcomeMessage = new SendMessage();
        welcomeMessage.setChatId(chatId);
        welcomeMessage.setText("Вывод кредитов");
        welcomeMessage.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Меню")))).build());
        botService.sendMessage(welcomeMessage);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Хотите вывести кредиты ?");

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Ввести сумму для вывода");
        button.setCallbackData("WITH_DRAWAL_OF_CREDITS");

        List<InlineKeyboardButton> buttons = List.of(button);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));

        message.setReplyMarkup(markup);
        botService.sendMessage(message);
    }
}
