package com.dehys.lythorioncore.jda.commands;

import com.dehys.lythorioncore.jda.Bot;
import net.dv8tion.jda.api.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public interface JDACommand {
    @NotNull
    String getName();

    @NotNull
    default CommandManager getManager() {
        return Bot.getCommandManager();
    }

    @NotNull
    default Collection<String> getAlias() {
        return new HashSet<>();
    }
    @NotNull
    default String getHelp() {
        return "No help found for this command.";
    }

    void execute(CommandInformation info);

    @NotNull
    default Collection<Permission> getRequiredPermissions() {
        return new HashSet<>();
    }
}