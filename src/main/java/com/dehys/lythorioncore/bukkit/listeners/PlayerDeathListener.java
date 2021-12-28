package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(org.bukkit.event.entity.PlayerDeathEvent e){
        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_RED, Channel.GLOBAL, null, e.getDeathMessage());
    }

}
