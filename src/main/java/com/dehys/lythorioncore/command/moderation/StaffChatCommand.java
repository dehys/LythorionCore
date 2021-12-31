package com.dehys.lythorioncore.command.moderation;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import com.dehys.lythorioncore.factory.StorageFactory;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StaffChatCommand implements GenericCommand {

    public StaffChatCommand() {
        Main.getCommandHandler.addCommand(this);
    }

    @Override
    public @NotNull String getName() {
        return "staffchat";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("sc");
        aliases.add("staff");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        String help = """
                Staff Chat Command
                Usage: /staffchat <message> - Sends a message to the staff chat
                Usage: /staffchat toggle - Toggles the staff chat
                """;
        return commandCaller == CommandCaller.MINECRAFT_PLAYER ? help : GenericCommand.super.getHelp(commandCaller);
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().split(" ")[0];
        String[] args = event.getMessage().replaceFirst(command, "").split(" ");

        if (args.length == 0) {
            event.getPlayer().sendMessage(ChatColor.RED + this.getHelp(CommandCaller.MINECRAFT_PLAYER));
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            if (StorageFactory.staffChat.contains(event.getPlayer().getUniqueId())) {
                StorageFactory.staffChat.remove(event.getPlayer().getUniqueId());
                event.getPlayer().sendMessage("§aYou are now sending messages to the global chat.");
            } else {
                StorageFactory.staffChat.add(event.getPlayer().getUniqueId());
                event.getPlayer().sendMessage("§cYou are now sending messages to the staff chat.");
            }
            return;
        }

        String message = String.join(" ", args);
        MessageUtil.sendMinecraftMessage(Channel.STAFF, event.getPlayer(), message);
    }

    @Override
    public void execute(ServerCommandEvent event) {
    }

    @Override
    public void execute(MessageReceivedEvent event) {
    }

    @Override
    public void execute(SlashCommandEvent event) {
    }
}