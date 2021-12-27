package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.bukkit.MessageUtil;
import com.dehys.lythorioncore.bukkit.commands.NickCommand;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Objects;


public class BukkitChatListener implements Listener {
    public BukkitChatListener(){}



    @EventHandler
    public void onPlayerChatEvent(AsyncPlayerChatEvent event){
        if(StorageFactory.staffChat.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            MessageUtil.broadcastMessage(Channel.STAFF, event.getPlayer(), event.getMessage());
        }
        event.setFormat(ChatColor.GRAY+event.getPlayer().getDisplayName()+": "+ChatColor.WHITE+event.getMessage());
        new DiscordChatListener(DiscordChatListener.ChatType.CHAT, event.getPlayer(), event.getMessage());
    }
}
