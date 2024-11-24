package com.kingleaks.king_credits.bot.state.admin.payment_details;

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
public class PaymentDetailsState implements Command {
    private final BotService botService;
    private final StateManagerService stateManager;

    @Override
    public void execute(Update update) {
        StatePaymentHistory userState = new StatePaymentHistory();
        userState.setTelegramUserId(update.getMessage().getFrom().getId());
        userState.setStatus("WAITING_FOR_PAYMENT_DETAILS");
        stateManager.setUserState(update.getMessage().getFrom().getId(), userState);

        SendMessage message = SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text("Чтобы обновить реквизиты, отправьте форму в таком формате\n" +
                        "Данные о банке и владельце - Сбер(Алекс А)\n" +
                        "Номер карты - 2202203605740234\n" +
                        "Пример формы: Сбер(Алекс А)_2202203605740234")
                .build();
        message.setReplyMarkup(ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(List.of(new KeyboardButton("\uD83D\uDCC3 Меню"))))
                .resizeKeyboard(true).build());

        botService.sendMessage(message);
    }
}
