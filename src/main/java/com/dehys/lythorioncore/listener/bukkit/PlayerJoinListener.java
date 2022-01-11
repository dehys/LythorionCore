package com.dehys.lythorioncore.listener.bukkit;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.object.tag.TagManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent e) {
        e.setJoinMessage(e.getPlayer().getDisplayName() + ChatColor.YELLOW + " joined the game");

        TagManager.reloadTeamForPlayer(e.getPlayer());

        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_GREEN, Channel.GLOBAL, e.getPlayer(), " joined the game");
    }

}
