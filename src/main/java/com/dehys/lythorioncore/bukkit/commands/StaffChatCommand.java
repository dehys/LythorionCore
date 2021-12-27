package com.dehys.lythorioncore.bukkit.commands;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.bukkit.MessageUtil;
import com.dehys.lythorioncore.factories.StorageFactory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if(args.length == 0) {
            player.sendMessage(ChatColor.RED + command.getUsage());
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            if (StorageFactory.staffChat.contains(player.getUniqueId())) {
                StorageFactory.staffChat.remove(player.getUniqueId());
                player.sendMessage("§aYou are now sending messages to the global chat.");
            } else {
                StorageFactory.staffChat.add(player.getUniqueId());
                player.sendMessage("§cYou are now sending messages to the staff chat.");
            }
            return true;
        }

        String message = String.join(" ", args);
        MessageUtil.broadcastMessage(Channel.STAFF, player, message);


        return true;
    }
}
