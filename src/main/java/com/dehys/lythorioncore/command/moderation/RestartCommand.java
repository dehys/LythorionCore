package com.dehys.lythorioncore.command.moderation;

import com.dehys.lythorioncore.core.Channel;
import com.dehys.lythorioncore.core.Main;
import com.dehys.lythorioncore.core.MessageUtil;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RestartCommand implements GenericCommand {

    public RestartCommand() {
        Main.commandHandler.addCommand(this);
    }

    @Override
    public List<CommandData> getCommandData() {
        List<CommandData> data = GenericCommand.super.getCommandData();
        for (CommandData d : data) {
            d.addOption(OptionType.INTEGER, "seconds", "Time to wait before restarting", false);
        }
        return data;
    }

    @Override
    public @NotNull String getName() {
        return "restart";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("stop");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        return "Restarts the server";
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
        restart();
    }

    @Override
    public void execute(ServerCommandEvent event) {
        restart();
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        restart();
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.deferReply().setEphemeral(true).complete();
        OptionMapping secondsOption = event.getOption("seconds");

        if (secondsOption == null) {
            event.getHook().sendMessage("Server will now restart").setEphemeral(true).complete();
            restart();
        } else {
            int seconds = (int) secondsOption.getAsDouble();
            event.getHook().sendMessage("Server will restart in **" + seconds + "** seconds").setEphemeral(true).complete();
            Bukkit.getScheduler().runTaskLater(Main.plugin, this::restart, seconds * 20L);
        }
    }

    @Override
    public Collection<Permission> getRequiredDiscordPermissions() {
        Collection<Permission> permissions = GenericCommand.super.getRequiredDiscordPermissions();
        permissions.add(Permission.ADMINISTRATOR);
        return permissions;
    }

    private void restart() {
        Bukkit.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.RED + "Server restarting"));
        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_BLUE, Channel.GLOBAL, null, "Restarting Server...");
        Main.bot.shutdown();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new RestartRunnable());
    }

    private static class RestartRunnable implements Runnable {
        @Override
        public void run() {
            Bukkit.getServer().shutdown();
        }
    }
}
