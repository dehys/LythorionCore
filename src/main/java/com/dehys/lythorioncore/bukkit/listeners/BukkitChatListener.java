package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.factories.StorageFactory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class BukkitChatListener implements Listener {
    public BukkitChatListener(){}


    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.setCancelled(true);

        String avatar = StorageFactory.AVATAR_PROVIDER_URL + event.getPlayer().getUniqueId() + "?size=128&overlay=true";

        if (StorageFactory.staffChat.contains(event.getPlayer().getUniqueId())) {
            MessageUtil.sendMinecraftMessage(Channel.STAFF, event.getPlayer(), event.getMessage());
            MessageUtil.sendDiscordWebhook(Channel.STAFF, event.getPlayer().getName(), event.getMessage(), avatar);
            return;
        }

        MessageUtil.sendMinecraftMessage(Channel.GLOBAL, event.getPlayer(), event.getMessage());
        MessageUtil.sendDiscordWebhook(Channel.GLOBAL, event.getPlayer().getName(), event.getMessage(), avatar);
    }
}
