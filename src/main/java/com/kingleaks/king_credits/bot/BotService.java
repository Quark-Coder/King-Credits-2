package com.kingleaks.king_credits.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {
    void sendMessage(SendMessage message);
}
