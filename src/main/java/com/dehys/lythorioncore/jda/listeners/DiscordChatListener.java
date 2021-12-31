package com.dehys.lythorioncore.jda.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.MessageUtil;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.Bot;
import com.dehys.lythorioncore.jda.commands.CommandManager;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordChatListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (CommandManager.isCommand(event.getMessage().getContentRaw())) return;
        if (event.getAuthor().equals(Bot.jda.getSelfUser())) return;
        if (event.getAuthor().isBot()) return;

        if (event.getGuild().getId().equals(StorageFactory.GUILD_ID)) {
            if (event.getChannel().getId().equalsIgnoreCase(StorageFactory.CHANNEL_ID)) {
                MessageUtil.sendMinecraftMessage(Channel.GLOBAL, Objects.requireNonNull(event.getMember()), event.getMessage().getContentRaw());
            } else if (event.getChannel().getId().equalsIgnoreCase(StorageFactory.LOG_CHANNEL_ID)) {
                MessageUtil.sendMinecraftMessage(Channel.STAFF, Objects.requireNonNull(event.getMember()), event.getMessage().getContentRaw());
            }
        }
    }
}