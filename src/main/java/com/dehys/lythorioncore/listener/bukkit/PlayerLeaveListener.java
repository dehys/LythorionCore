package com.dehys.lythorioncore.listener.bukkit;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        e.setQuitMessage(e.getPlayer().getDisplayName() + ChatColor.YELLOW + " left the game");

        try {
            MessageUtil.sendDiscordEmbed(Channel.GLOBAL, e.getPlayer(), " left the game");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
