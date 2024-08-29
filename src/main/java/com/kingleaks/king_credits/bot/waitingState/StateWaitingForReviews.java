package com.kingleaks.king_credits.bot.waitingState;

import com.kingleaks.king_credits.domain.entity.StatePaymentHistory;
import com.kingleaks.king_credits.service.ReviewsService;
import com.kingleaks.king_credits.service.StateManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class StateWaitingForReviews {
    private final StateManagerService stateManager;
    private final ReviewsService reviewsService;

    public SendMessage waitingForReviews(StatePaymentHistory paymentHistory,
                                              Long chatId, String messageText, Long telegramUserID, Long photoId){
        if (paymentHistory != null){
            reviewsService.addComment(photoId, messageText);

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText("Спасибо за ваш отзыв!");
            stateManager.deleteUserState(telegramUserID);
            return message;
        }
        return null;
    }
}
