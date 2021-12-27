package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvanceListener implements Listener {

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent e){
        String k = e.getAdvancement().getKey().toString();
        if (k.startsWith("minecraft:story") || k.startsWith("minecraft:nether") || k.startsWith("minecraft:end") || k.startsWith("minecraft:adventure") || k.startsWith("minecraft:husbandry")){
            if (k.split("/")[1].equalsIgnoreCase("root")) return;
            new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_ADVANCEMENT, e.getPlayer(), "got the advancement:", e.getAdvancement());
        }
    }

}
