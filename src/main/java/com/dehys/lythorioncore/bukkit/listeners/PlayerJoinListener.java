package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event){
        event.setJoinMessage(event.getPlayer().getDisplayName()+ChatColor.YELLOW+" joined the game");
        new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_JOIN, event.getPlayer(), " joined the game");
    }

}
