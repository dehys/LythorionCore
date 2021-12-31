package com.dehys.lythorioncore.jda.commands.utilities;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.jda.commands.CommandInformation;
import com.dehys.lythorioncore.jda.commands.JDACommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayersCommand implements JDACommand {

    @Override
    public @NotNull String getName() {
        return "players";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> alias = new HashSet<>();
        alias.add("pl");
        return alias;
    }

    @Override
    public @NotNull String getHelp() {
        return "Show's the players online.";
    }

    @Override
    public void execute(CommandInformation info) {
        if (info.isGuild()) execute(info.getGuildEvent());
    }

    private void execute(GuildMessageReceivedEvent event){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x00FF00);

        StringBuilder playerView = new StringBuilder();
        Collection<? extends Player> playersOnline = Bukkit.getOnlinePlayers();
        playerView.append(playersOnline.size() == 0 ? "No players online :(" : "__Players online:__ \n");

        for (Player player : playersOnline) {
            playerView.append(player.getDisplayName()).append("\n");
        }

        embedBuilder.setDescription(playerView.toString());

        MessageUtil.sendDiscordMessage(Channel.GLOBAL, embedBuilder.build());
    }
}
