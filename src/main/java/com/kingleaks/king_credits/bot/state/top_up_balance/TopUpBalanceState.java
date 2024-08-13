package com.kingleaks.king_credits.bot.state.top_up_balance;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class TopUpBalanceState implements Command {
    private final BotService botService;

    public String getDescription() {
        return "Здесь вы можете пополнить баланс";
    }

    @Autowired
    public TopUpBalanceState(BotService botService) {
        this.botService = botService;
    }

    @Override
    public void execute(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage welcomeMessage = new SendMessage();
        welcomeMessage.setChatId(chatId);
        welcomeMessage.setText(getDescription());
        botService.sendMessage(welcomeMessage);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите опцию");

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Пополнить баланс");
        button.setCallbackData("TOP_UP_BUTTON");

        List<InlineKeyboardButton> buttons = List.of(button);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(buttons));
        message.setReplyMarkup(markup);

        botService.sendMessage(message);
    }
}
