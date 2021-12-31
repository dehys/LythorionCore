package com.dehys.lythorioncore.command;

import com.dehys.lythorioncore.factory.StorageFactory;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandHandler extends ListenerAdapter implements Listener {

    public static List<GenericCommand> commands = new ArrayList<>();

    public void addCommand(GenericCommand command) {
        commands.add(command);
    }

    public void removeCommand(String commandName) {
        commands.stream().filter(command -> command.getName().equalsIgnoreCase(commandName)).forEach(commands::remove);
    }

    public void removeCommand(GenericCommand command) {
        commands.remove(command);
    }

    public HashSet<GenericCommand> getCommands() {
        return new HashSet<>(commands);
    }

    private GenericCommand getCommand(String name) {
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name) || command.getAlias().contains(name.toLowerCase())).findFirst().orElse(null);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        GenericCommand command = getValidCommand(event.getAuthor(), event.getMessage().getContentRaw());
        if (command == null) return;

        if (event.isFromType(ChannelType.TEXT)) {
            Collection<GenericPermission> permissions = command.getRequiredPermissions();
            permissions.forEach(permission -> {
                if (permission)
            });

            if (!(Objects.requireNonNull(event.getMember()).hasPermission(command.getRequiredDiscordPermissions()))) {
                event.getChannel().sendMessage("You do not have permission to use this command.").complete();
                return;
            }
        }

        switch (event.getChannelType()) {
            case PRIVATE -> command.execute(new CommandInformation(CommandCaller.DISCORD_PRIVATE.setEventObject(event)));
            case TEXT -> command.execute(new CommandInformation(CommandCaller.DISCORD_TEXT.setEventObject(event)));
        }
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        GenericCommand command = getValidCommand(event.getUser(), event.getName());
        if (command == null) return;

        System.out.println("A VALID COMMAND WAS FOUND AND RAN BY A DISCORD SLASH COMMAND");

        command.execute(new CommandInformation(CommandCaller.DISCORD_SLASH.setEventObject(event)));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        GenericCommand command = getValidCommand(null, event.getMessage());
        if (command == null) return;

        command.execute(new CommandInformation(CommandCaller.MINECRAFT_PLAYER.setEventObject(event)));
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event) {
        GenericCommand command = getValidCommand(null, event.getCommand());
        if (command == null) return;

        command.execute(new CommandInformation(CommandCaller.MINECRAFT_CONSOLE.setEventObject(event)));
    }

    public GenericCommand getValidCommand(User user, String s) {
        boolean isBukkit = user == null;
        if (!isBukkit && user.isBot()) return null;

        String prefix = isBukkit ? "/" : StorageFactory.DISCORD_PREFIX;

        String[] split = s
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        return getCommand(split[0]);
    }

}