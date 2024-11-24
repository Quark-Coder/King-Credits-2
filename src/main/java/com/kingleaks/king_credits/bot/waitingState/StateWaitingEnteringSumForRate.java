package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.repository.AccountRepository;
import com.kingleaks.king_credits.service.HeadAndTailService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StateWaitingEnteringSumForRate implements StateWaitingQueryHandler{
    private final StateManagerService stateManager;
    private final AccountRepository accountRepository;

    public SendMessage waitingForEnteringSumForRate(StatePaymentHistory paymentHistory,
                                                 Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                int amount = Integer.parseInt(messageText);
                if (amount > 5000){
                    message.setText("Сумма не должна быть выше 5000, введите еще раз свою сумму");
                    return message;
                } else if (accountRepository.getBalanceAccountsByTelegramUserId(telegramUserID) < amount) {
                    message.setText("У вас на балансе нехватает нужной суммы для игры");
                    return message;
                } else if (amount <= 0) {
                    message.setText("Сумма не может быть равно 0 или ниже его");
                    return message;
                }

                message.setText("❗\uFE0FДеньги списаны с баланса. \n" +
                        "\n" +
                        "Выбирайте, орел или решка! Все или ничего…\n");

                InlineKeyboardButton head = new InlineKeyboardButton();
                head.setText("Орел");
                head.setCallbackData("HEAD_CALLBACK__"+messageText);

                InlineKeyboardButton tail = new InlineKeyboardButton();
                tail.setText("Решка");
                tail.setCallbackData("HEAD_CALLBACK__"+messageText);

                List<InlineKeyboardButton> buttons = List.of(head, tail);
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                markup.setKeyboard(List.of(buttons));

                message.setReplyMarkup(markup);

                stateManager.deleteUserState(telegramUserID);

                return message;
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: невозможно преобразовать строку в целое число.");
                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_ENTERING_SUM_FOR_RATE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForEnteringSumForRate(paymentHistory, chatId, messageText, telegramUserID);
    }
}
