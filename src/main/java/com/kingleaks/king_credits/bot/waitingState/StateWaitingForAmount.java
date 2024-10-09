package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.PaymentCheckPhotoService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForAmount implements StateWaitingQueryHandler {
    private final PaymentCheckPhotoService paymentCheckPhotoService;
    private final StateManagerService stateManager;

    public SendMessage waitingForAmount(StatePaymentHistory paymentHistory,
                                       Long chatId, String messageText, Long telegramUserID){
        if (paymentHistory != null){
            try {
                double amount = Double.parseDouble(messageText);
                paymentCheckPhotoService.createPaymentCheckPhoto(telegramUserID, amount);

                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("\uD83D\uDCCD Для продолжения, оплатите товар любым удобным способом! \n" +
                        "• Сбербанк (Иван. А)\n" +
                        "• Карта: 2202203605740234\n" +
                        "\n" +
                        "• Другие способы оплаты - @DreamCredits\n" +
                        "\n" +
                        "\n<a href=\"https://telegra.ph/Usloviya-pered-pokupkoj--prodazhej-09-19\">" +
                        "Продолжая, вы автоматически соглашаетесь с условиями – нажми для ознакомления</a>"+
                        "\nПосле оплаты отправьте изображение чека");
                message.setParseMode("HTML");

                paymentHistory.setStatus("WAITING_FOR_PAYMENT_CHECK");
                stateManager.setUserState(telegramUserID, paymentHistory);

                return message;
            } catch (NumberFormatException e) {
                SendMessage message = new SendMessage();
                message.setChatId(chatId);
                message.setText("Ошибка: введите корректную сумму.");

                return message;
            }
        }
        return null;
    }

    @Override
    public boolean canHandle(String stateStatus) {
        return "WAITING_FOR_AMOUNT".equals(stateStatus);
    }

    @Override
    public SendMessage handle(StatePaymentHistory paymentHistory, Long chatId, String messageText, Long telegramUserID) {
        return waitingForAmount(paymentHistory, chatId, messageText, telegramUserID);
    }
}
