package me.piggypiglet.docdex.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import me.piggypiglet.docdex.commands.implementations.HelpCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

// ------------------------------
// Copyright (c) PiggyPiglet 2020
// https://www.piggypiglet.me
// ------------------------------
@Singleton
public final class CommandHandler {
    private final Set<Command> commands;
    private final Command unknownCommand;

    @Inject
    public CommandHandler(@NotNull @Named("commands") final Set<Command> commands, @NotNull final HelpCommand unknownCommand) {
        this.commands = commands;
        this.unknownCommand = unknownCommand;

        commands.add(unknownCommand);
    }

    public void process(@NotNull final String text) {
        final Command command = commands.stream()
                .filter(cmd -> cmd.getPrefix().equalsIgnoreCase(text))
                .findAny().orElse(unknownCommand);

        command.execute();
    }

    @NotNull
    public Set<Command> getCommands() {
        return commands;
    }
}