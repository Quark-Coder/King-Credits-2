package com.kingleaks.king_credits.bot.state.current_rate;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.service.SellingRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentRateState implements Command {
    private final BotService botService;
    private final SellingRateService sellingRateService;

    @Override
    public void execute(Update update) {
        String sellingRate = sellingRateService.getSellingRate();

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(sellingRate)
                .build();

        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());
        botService.sendMessage(message);
    }
}
