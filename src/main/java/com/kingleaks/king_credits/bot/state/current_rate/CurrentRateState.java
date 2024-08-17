package com.kingleaks.king_credits.bot.state.current_rate;

import com.kingleaks.king_credits.bot.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CurrentRateState implements Command {
    @Override
    public void execute(Update update) {

    }
}
