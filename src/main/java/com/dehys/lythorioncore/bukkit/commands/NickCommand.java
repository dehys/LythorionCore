package com.dehys.lythorioncore.bukkit.commands;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            return true;
        }

        //Minecraft nickname command
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "/nick <nickname>");
            return true;
        }

        if (args.length == 1) {
            //set nickname of player
            StringBuilder nickname = new StringBuilder();
            for (String a : args) {
                nickname.append(a);
            }

            if (nickname.toString().length() > 16) {
                sender.sendMessage(ChatColor.RED + "Nickname is too long!");
                return true;
            }

            player.setDisplayName(colorize(nickname.toString()));
            sender.sendMessage(ChatColor.GREEN + "Nickname set to " + args[0]);
        }

        return true;
    }

    private static final Pattern HEX_PATTERN = Pattern.compile("&(#\\w{6})");

    public static String colorize(String message) {
        Matcher matcher = HEX_PATTERN.matcher(ChatColor.translateAlternateColorCodes('&', message));
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
        }

        return matcher.appendTail(buffer).toString();
    }
}
