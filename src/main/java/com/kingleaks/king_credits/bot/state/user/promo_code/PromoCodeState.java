package com.kingleaks.king_credits.bot.state.user.promo_code;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.bot.command.Command;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.StateManagerService;
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
public class PromoCodeState implements Command {
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public void execute(Update update) {
        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Введите промокод, который у вас есть")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("Назад")))).build());

        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(update.getMessage().getFrom().getId());
        userState.setStatus("WAITING_FOR_ENTER_PROMOCODE");
        stateManager.setUserState(update.getMessage().getFrom().getId(), userState);

        botService.sendMessage(message);
    }
}
