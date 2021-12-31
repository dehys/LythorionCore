package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        e.setJoinMessage(e.getPlayer().getDisplayName() + ChatColor.YELLOW + " joined the game");

        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_GREEN, Channel.GLOBAL, e.getPlayer(), " joined the game");
    }

}
