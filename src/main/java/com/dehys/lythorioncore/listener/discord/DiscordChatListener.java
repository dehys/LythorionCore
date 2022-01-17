package com.dehys.lythorioncore.listener.discord;

import com.dehys.lythorioncore.core.Bot;
import com.dehys.lythorioncore.core.Channel;
import com.dehys.lythorioncore.core.MessageUtil;
import com.dehys.lythorioncore.factory.StorageFactory;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DiscordChatListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT)) return;
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