package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.PromoCode;
import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PromoCodeService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
@RequiredArgsConstructor
public class StateWaitingCreatePromoCode implements StateWaitingQueryHandler {
    private final StateManagerService stateManager;
    private final PromoCodeService promoCodeService;

    public SendMessage waitingForCreatePromoCode(StatePaymentHistory paymentHistory,
                                        Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                String[] parts = messageText.split("_");
                String timeStamp = parts[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

                String code = parts[0];
                LocalDate endDate = LocalDate.parse(timeStamp, formatter);
                int numberOfUses = Integer.parseInt(parts[2]);
                double amount = Double.parseDouble(parts[3]);

                PromoCode promoCode = promoCodeService.createPromoCode(code, endDate, numberOfUses, amount);

                if (promoCode == null){
                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Ошибка: такой промокод уже существует");
                    return message;
                }

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Хорошо, промокод активен");

                stateManager.deleteUserState(telegramUserID);

                return message;
            } catch (DateTimeParseException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: некорректный формат даты.");
                return message;
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: невозможно преобразовать строку в целое число.");
                return message;
            } catch (ArrayIndexOutOfBoundsException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: Вы пропустили одну из полей");
                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_CREATE_PROMOCODE".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForCreatePromoCode(paymentHistory, chatId, messageText, telegramUserID);
    }
}
