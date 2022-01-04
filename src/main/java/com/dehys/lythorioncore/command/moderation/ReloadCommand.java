package com.dehys.lythorioncore.command.moderation;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import com.dehys.lythorioncore.command.GenericPermission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class ReloadCommand implements GenericCommand {

    public ReloadCommand() {
        Main.getCommandHandler.addCommand(this);
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("rl");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        return "Reloads the server";
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
        event.getPlayer().sendMessage(ChatColor.RED + "Reloading the server...");
        reload();
    }

    @Override
    public void execute(ServerCommandEvent event) {
        reload();
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            event.getChannel().sendMessage("**[!]** This command cannot be used in private message channels.").complete();
            return;
        }

        reload();
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.deferReply().setEphemeral(true).complete();
        event.getHook().sendMessage("Server will now reload.").setEphemeral(true).complete();
        reload();
    }

    @Override
    public Collection<GenericPermission> getRequiredPermissions() {
        Collection<GenericPermission> permissions = GenericCommand.super.getRequiredPermissions();
        permissions.add(GenericPermission.ADMINISTRATOR);
        return permissions;
    }

    private void reload() {
        Main.getPlugin.getLogger().log(Level.SEVERE, "Reloading the server...");
        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_BLUE, Channel.GLOBAL, null, "Reloading server...");
        Main.getBot.shutdown();
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin, new ReloadRunnable());
    }

    private static class ReloadRunnable implements Runnable {
        @Override
        public void run() {
            Bukkit.getServer().reload();
        }
    }
}
