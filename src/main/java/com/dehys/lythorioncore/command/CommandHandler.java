package com.dehys.lythorioncore.command;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.factory.StorageFactory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.logging.Level;
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

        //Check permissions
        if (event.isFromType(ChannelType.TEXT)) {
            if (!this.getPermissions(CommandCaller.DISCORD_TEXT, command).stream().allMatch(p -> Objects.requireNonNull(event.getMember()).hasPermission((Permission) p))) {
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

        if (event.getChannelType() == ChannelType.TEXT) {
            if (!this.getPermissions(CommandCaller.DISCORD_SLASH, command).stream().allMatch(p -> Objects.requireNonNull(event.getMember()).hasPermission((Permission) p))) {
                event.getChannel().sendMessage("You do not have permission to use this command.").complete();
                return;
            }
        }

        command.execute(new CommandInformation(CommandCaller.DISCORD_SLASH.setEventObject(event)));
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String commandLabel = event.getMessage().split(" ")[0].replace("/", "");
        Command bukkitCommand = Bukkit.getServer().getPluginCommand(commandLabel.toLowerCase());
        if (bukkitCommand == null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED+ "Not a valid command.");
            return;
        }

        GenericCommand genericCommand = getValidCommand(null, event.getMessage());
        if (genericCommand != null) {
            genericCommand.execute(new CommandInformation(CommandCaller.MINECRAFT_PLAYER.setEventObject(event)));
        }
    }

    @EventHandler
    public void onServerCommandEvent(ServerCommandEvent event) {
        Command bukkitCommand = Bukkit.getServer().getPluginCommand(event.getCommand());
        if (bukkitCommand == null) {
            event.setCancelled(true);
            Main.plugin.getLogger().log(Level.WARNING, "Not a valid command.");
            return;
        }

        GenericCommand genericCommand = getValidCommand(null, event.getCommand());
        if (genericCommand != null) {
            genericCommand.execute(new CommandInformation(CommandCaller.MINECRAFT_CONSOLE.setEventObject(event)));
        }
    }

    public Collection<Object> getPermissions(CommandCaller caller, GenericCommand command){
        Collection<Object> requiredPermissions = new ArrayList<>();
        if (caller == CommandCaller.DISCORD_TEXT || caller == CommandCaller.DISCORD_SLASH) {
            for (GenericPermission p : command.getRequiredPermissions()) {
                if (!p.isBukkit()){
                    requiredPermissions.add(GenericPermission.getAsDiscordPermission(p));
                }
            }
            return requiredPermissions;
        }else if (caller == CommandCaller.MINECRAFT_PLAYER || caller == CommandCaller.MINECRAFT_CONSOLE) {
            for (GenericPermission p : command.getRequiredPermissions()) {
                if (p.isBukkit()){
                    requiredPermissions.add(GenericPermission.getAsBukkitPermission(p));
                }
            }
        }else {
            return null;
        }
        return requiredPermissions;
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