package com.dehys.lythorioncore.bukkit.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class PlayerAdvanceListener implements Listener {

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent e){
        String k = e.getAdvancement().getKey().toString();
        if (k.startsWith("minecraft:story") || k.startsWith("minecraft:nether") || k.startsWith("minecraft:end") || k.startsWith("minecraft:adventure") || k.startsWith("minecraft:husbandry")){
            if (k.split("/")[1].equalsIgnoreCase("root")) return;
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_BLUE, Channel.GLOBAL, e.getPlayer(), "got the advancement:", e.getAdvancement());
        }
    }

}
