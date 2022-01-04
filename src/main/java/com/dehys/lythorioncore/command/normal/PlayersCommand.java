package com.dehys.lythorioncore.command.normal;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlayersCommand implements GenericCommand {

    public PlayersCommand() {
        Main.getCommandHandler.addCommand(this);
    }

    @Override
    public @NotNull String getName() {
        return "players";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("playerlist");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        return "Lists all players on the server.";
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
    }

    @Override
    public void execute(ServerCommandEvent event) {
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        event.getChannel().sendMessageEmbeds(getEmbed()).complete();
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.deferReply().complete();
        event.getHook().sendMessageEmbeds(getEmbed()).complete();
    }

    private MessageEmbed getEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x00FF00);

        StringBuilder playerView = new StringBuilder();
        Collection<? extends Player> playersOnline = Bukkit.getOnlinePlayers();
        playerView.append(playersOnline.size() == 0 ? "No players online :(" : "__Players online:__ \n");

        for (Player player : playersOnline) {
            playerView.append(player.getDisplayName()).append("\n");
        }

        embedBuilder.setDescription(playerView.toString());

        return embedBuilder.build();
    }
}
