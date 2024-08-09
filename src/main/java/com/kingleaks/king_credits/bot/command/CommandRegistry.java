package com.kingleaks.king_credits.bot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandRegistry {
    private final Map<String, Command> commandMap = new HashMap<>();

    @Autowired
    public CommandRegistry(List<Command> commands) {
        for (Command command : commands) {
            commandMap.put(command.getClass().getSimpleName().toLowerCase(), command);
        }
    }

    public Command getCommand(String commandName) {
        return commandMap.get(commandName.toLowerCase());
    }
}
