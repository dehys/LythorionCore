package com.dehys.lythorioncore.jda.features;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.jda.Bot;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;

public class DescriptionUpdate {

    public DescriptionUpdate(){

        // Update the description every 30 seconds
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin, () -> {
            int playerCount = Bukkit.getServer().getOnlinePlayers().size();
            int maxPlayers = Bukkit.getServer().getMaxPlayers();
            TextChannel channel = Bot.channel;
            channel.getManager().setTopic("\uD83D\uDD17 play.lythorion.com | \uD83D\uDC65 "+playerCount+"/"+maxPlayers).queue();
        }, 0, 15 * 20);

    }

}
