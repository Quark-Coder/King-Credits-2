package com.kingleaks.king_credits.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionVerificationService {
    @Value("${telegram.channel.username}")
    private String channelUsername;

    private final AbsSender absSender;

    public boolean verifySubscription(Long telegramUserId) {
        GetChatMember getChatMember = new GetChatMember();
        getChatMember.setUserId(telegramUserId);
        getChatMember.setChatId(channelUsername);

        try {
            ChatMember chatMember = absSender.execute(getChatMember);
            String status = chatMember.getStatus();

            return "member".equals(status) ||
                    "administrator".equals(status) ||
                    "creator".equals(status);
        } catch (TelegramApiException e){
            log.error("Ошибка с chatMember", e.getMessage());
            return false;
        }

    }


}
