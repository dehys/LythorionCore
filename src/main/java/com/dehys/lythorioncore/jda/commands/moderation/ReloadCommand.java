package com.dehys.lythorioncore.jda.commands.moderation;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.jda.commands.CommandInformation;
import com.dehys.lythorioncore.jda.commands.JDACommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ReloadCommand implements JDACommand {

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> alias = new HashSet<>();
        alias.add("rl");
        return alias;
    }

    @Override
    public @NotNull Collection<Permission> getRequiredPermissions(){
        HashSet<Permission> set = new HashSet<>();
        set.add(Permission.BAN_MEMBERS);
        return set;
    }

    @Override
    public @NotNull String getHelp() {
        return "Reloads the server";
    }

    @Override
    public void execute(CommandInformation info) {
        if (info.isGuild()) execute(info.getGuildEvent());
    }

    private void execute(GuildMessageReceivedEvent event){
        MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_BLUE, Channel.GLOBAL, "Reloading server...");
        Main.getPlugin.getServer().reload();
    }
}
