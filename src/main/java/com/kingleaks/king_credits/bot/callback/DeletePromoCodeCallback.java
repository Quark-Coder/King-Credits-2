package com.kingleaks.king_credits.bot.callback;

import com.kingleaks.king_credits.bot.BotService;
import com.kingleaks.king_credits.service.PromoCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@Component
@RequiredArgsConstructor
public class DeletePromoCodeCallback implements CallbackQueryHandler{
    private final PromoCodeService promoCodeService;
    private final BotService botService;

    @Override
    public boolean canHandle(String callbackData) {
        String[] parts = callbackData.split("__");
        if (parts[0].equals("DELETE_PROMOCODE")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        String[] parts = callbackQuery.getData().split("__");
        Long idPromoCode = Long.parseLong(parts[1]);
        promoCodeService.deletePromoCodeById(idPromoCode);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Промокод удален");
        botService.sendMessage(sendMessage);
    }
}
