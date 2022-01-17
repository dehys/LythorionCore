package com.dehys.lythorioncore.command;

import com.dehys.lythorioncore.core.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface GenericCommand {

    default List<CommandData> getCommandData() {
        List<CommandData> data = new ArrayList<>();
        data.add(new CommandData(this.getName(), this.getHelp(CommandCaller.DISCORD_SLASH)));
        for (String alias : getAlias()) {
            data.add(new CommandData(alias, this.getHelp(CommandCaller.DISCORD_SLASH)));
        }
        return data;
    }

    default GenericCommand getParent() {
        return null;
    }

    @NotNull
    String getName();

    @NotNull
    default CommandHandler getManager() {
        return Main.commandHandler;
    }

    @NotNull
    default Collection<String> getAlias() {
        return new HashSet<>();
    }

    @NotNull
    default String getHelp(CommandCaller commandCaller) {
        return "No help found for this command.";
    }

    default void execute(CommandInformation ci) {
        switch (ci.getCaller()) {
            case MINECRAFT_PLAYER -> execute((PlayerCommandPreprocessEvent) ci.getEventObject());
            case MINECRAFT_CONSOLE -> execute((ServerCommandEvent) ci.getEventObject());
            case DISCORD_PRIVATE, DISCORD_TEXT -> execute((MessageReceivedEvent) ci.getEventObject());
            case DISCORD_SLASH -> execute((SlashCommandEvent) ci.getEventObject());
        }
    }

    void execute(PlayerCommandPreprocessEvent event);

    void execute(ServerCommandEvent event);

    void execute(MessageReceivedEvent event);

    void execute(SlashCommandEvent event);

    default Collection<Permission> getRequiredDiscordPermissions() {
        Set<Permission> permissions = new HashSet<>();
        permissions.add(Permission.MESSAGE_SEND);
        return permissions;
    }
}
