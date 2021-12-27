package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e){
        e.setQuitMessage(e.getPlayer().getDisplayName()+ ChatColor.YELLOW+" left the game");
        new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_LEAVE, e.getPlayer(), " left the game");
    }

}
