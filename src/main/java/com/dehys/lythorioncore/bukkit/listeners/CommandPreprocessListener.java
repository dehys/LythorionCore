package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
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
            new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_ADVANCEMENT, null, "Reloading Server...");
        }
    }
}
