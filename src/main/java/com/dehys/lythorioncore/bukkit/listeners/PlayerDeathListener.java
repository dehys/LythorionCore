package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent e){
        new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_DEATH, null, e.getDeathMessage());
    }

}
