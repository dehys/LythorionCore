package com.dehys.lythorioncore.feature;

import com.dehys.lythorioncore.Main;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;

public class DescriptionUpdate {

    public static void run() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin, () -> {
            int playerCount = Bukkit.getServer().getOnlinePlayers().size();
            int maxPlayers = Bukkit.getServer().getMaxPlayers();
            TextChannel channel = Main.getBot.getChannel();
            channel.getManager().setTopic("\uD83D\uDD17 play.lythorion.com | \uD83D\uDC65 " + playerCount + "/" + maxPlayers).complete();
        }, 0, 15 * 20);
    }

}
