package com.dehys.lythorioncore.bukkit;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.bukkit.commands.NickCommand;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MessageUtil {

    public static void broadcastDiscordMessage(@NotNull Channel channel, @NotNull Member member, @NotNull String message) {
            String hexColor = "&#"+Integer.toHexString(Objects.requireNonNull(member.getColor()).getRGB()).substring(2);
            switch (channel) {
                case STAFF -> Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("lythorion.staffchat")).forEach(player ->
                        player.sendMessage("§7[§cStaff§7] " + member.getEffectiveName() + "§8: §f" + message));
                case GLOBAL -> Main.getPlugin.getServer().broadcastMessage(ChatColor.BLUE+"[D] "+ NickCommand.colorize(hexColor)+member.getEffectiveName()+"§8: "+ChatColor.WHITE+message);
            }
    }

    public static void broadcastMessage(@NotNull Channel channel, @NotNull Player sender, @NotNull String message) {
        switch (channel) {
            case STAFF -> Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("lythorion.staffchat")).forEach(player ->
                    player.sendMessage("§7[§cStaff§7] " + sender.getDisplayName() + "§8: §f" + message));
            case GLOBAL -> {/*TODO: Add format for global message*/}
        }
    }




}
