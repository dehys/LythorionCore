package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandPreprocessListener implements Listener {

    public static boolean reloading = false;

    @EventHandler
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage();
        if (command.startsWith("/reload") || command.startsWith("/rl")) {
            reloading = true;
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_BLUE, Channel.GLOBAL, null, "Reloading Server...");
        }
    }
}
