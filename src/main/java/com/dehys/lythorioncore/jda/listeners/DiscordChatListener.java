package com.dehys.lythorioncore.jda.listeners;

import com.dehys.lythorioncore.Channel;
import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.bukkit.MessageUtil;
import com.dehys.lythorioncore.bukkit.listeners.BukkitChatListener;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class DiscordChatListener extends ListenerAdapter {

    public DiscordChatListener(){}

    public DiscordChatListener(ChatType chatType, Object ... entries) {
        Player player = null;
        String content = null;
        String advancement = null;

        try{
            if (entries[0] != null) {
                player = (Player) entries[0];
            }
            if (entries[1] != null){
                content = (String) entries[1];
            }
            if (entries[2] != null) {
                advancement = ((Advancement) entries[2]).getKey().toString().replace(
                        ((Advancement) entries[2]).getKey().toString().startsWith("minecraft:story") ? "minecraft:story/" :
                                ((Advancement) entries[2]).getKey().toString().startsWith("minecraft:nether") ? "minecraft:nether/" :
                                        ((Advancement) entries[2]).getKey().toString().startsWith("minecraft:end") ? "minecraft:end/" :
                                                ((Advancement) entries[2]).getKey().toString().startsWith("minecraft:adventure") ? "minecraft:adventure/" :
                                                        ((Advancement) entries[2]).getKey().toString().startsWith("minecraft:husbandry") ? "minecraft:husbandry/" :
                                                                "", "").replaceAll("_", " ");
            }
        } catch (ArrayIndexOutOfBoundsException ignored){}


        String crafatar_url = "https://crafatar.com/renders/head/";

        if (chatType == ChatType.CHAT){
            assert player != null;
            sendPost(player.getDisplayName(), content, crafatar_url+player.getUniqueId()+"?size=128&overlay=true");
            return;
        } else if (chatType == ChatType.PLAYER_SHOWITEM){ //TODO: Add item display
            //sendMessage(player.getDisplayName()+" is showing off an item: "+item);
            return;
        } else if (chatType == ChatType.PLAYER_SHOWINVETORY){ //TODO: Add inventory display
            //sendMessage(player.getDisplayName()+" is showing their inventory: "+inventory);
            return;
        } else if (chatType == ChatType.EMPTY){
            sendMessage(new MessageBuilder().append(content).build());
            return;
        }

        Message embed = new MessageBuilder()
                .setEmbed(
                        new EmbedBuilder()
                        .setColor(
                                chatType == ChatType.PLAYER_JOIN ? new Color(83, 252, 100) :
                                chatType == ChatType.PLAYER_LEAVE ? new Color(252, 83, 86) :
                                chatType == ChatType.PLAYER_DEATH ? new Color(252, 83, 86) :
                                chatType == ChatType.PLAYER_ADVANCEMENT ? new Color(83, 156, 252) :
                                        Color.PINK
                        )
                        .setFooter(
                                (player != null ? ChatColor.stripColor(player.getDisplayName())+" " : "")+(content != null ? content : "")+(advancement != null ? " "+advancement : ""),
                                (player != null ? crafatar_url+player.getUniqueId()+"?size=128&overlay=true" : "https://i.imgur.com/ahJ2DJ8.png"))
                        .build()
                ).build();
        sendMessage(embed);
    }

    public void sendMessage(Message message){
        Bot.channel.sendMessage(message).queue();
    }

    public void sendMessage(MessageEmbed message){
        Bot.channel.sendMessage(message).queue();
    }


    public void sendPost(String username, String content, String avatar_url)  {
        String url = StorageFactory.WEBHOOK_URL;

        HttpsURLConnection httpClient;
        try {
            httpClient = (HttpsURLConnection) new URL(url).openConnection();

            //add reuqest header
            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "username="+username+"&content="+content+"&avatar_url="+avatar_url;

            // Send post request
            httpClient.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream())) {
                wr.writeBytes(urlParameters);
                wr.flush();
            }

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getInputStream()))) {

                String line;
                StringBuilder response = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().equals(Main.discordBot.getJDA().getSelfUser())) return;
        if (event.getAuthor().isBot()) return;

        if (event.getGuild().getId().equals(Bot.guild.getId()) && event.getChannel().getId().equalsIgnoreCase(Bot.channel.getId())){
            String content = event.getMessage().getContentRaw();

            if (content.startsWith("!restart") || content.startsWith("!stop")){
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)){
                    Bukkit.shutdown();
                }else{
                    event.getChannel().sendMessage("You don't have permission to do that! (PS: Blame lasse...)").queue();
                }
                return;
            }

            MessageUtil.broadcastDiscordMessage(Channel.GLOBAL, event.getMember(), event.getMessage().getContentRaw());
        }
    }

    public enum ChatType {
        CHAT,
        PLAYER_LEAVE,
        PLAYER_JOIN,
        PLAYER_ADVANCEMENT,
        PLAYER_DEATH,
        PLAYER_SHOWITEM,
        PLAYER_SHOWINVETORY,
        PLAYER_NICKNAME,
        EMPTY
    }

}