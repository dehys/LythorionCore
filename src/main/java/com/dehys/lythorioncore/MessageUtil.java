package com.dehys.lythorioncore;

import com.dehys.lythorioncore.bukkit.commands.NickCommand;
import com.dehys.lythorioncore.bukkit.commands.ShowItemCommand;
import com.dehys.lythorioncore.bukkit.listeners.PlayerAdvanceListener;
import com.dehys.lythorioncore.bukkit.listeners.PlayerDeathListener;
import com.dehys.lythorioncore.bukkit.listeners.PlayerJoinListener;
import com.dehys.lythorioncore.bukkit.listeners.PlayerLeaveListener;
import com.dehys.lythorioncore.factories.StorageFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import java.util.logging.Level;

public class MessageUtil {

    public static void sendMinecraftMessage(@NotNull Channel channel, @NotNull Member member, @NotNull String message) {
        String hexColor = "&#" + Integer.toHexString(Objects.requireNonNull(member.getColor()).getRGB()).substring(2);

        switch (channel) {
            case STAFF -> Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("lythorion.staffchat")).forEach(player ->
                    player.sendMessage("§7[§cStaff§7] " + member.getEffectiveName() + ": §f" + message));

            case GLOBAL -> Main.getPlugin.getServer().broadcastMessage(ChatColor.BLUE + "[D] " + NickCommand.colorize(hexColor) + member.getEffectiveName() + ": " + ChatColor.WHITE + message);
        }
    }

    public static void sendMinecraftMessage(@NotNull Channel channel, @NotNull Player sender, @NotNull String message) {
        Main.getPlugin.getLogger().log(Level.INFO, "Player " + sender.getName() + " sent message: \"" + message + "\" in Channel " + channel.name());

        switch (channel) {
            case STAFF -> Bukkit.getOnlinePlayers().stream().filter(player -> player.hasPermission("lythorion.staffchat")).forEach(player ->
                    player.sendMessage("§7[§cStaff§7] " + sender.getDisplayName() + ": §f" + message));

            case GLOBAL -> Bukkit.broadcastMessage(ChatColor.GRAY + sender.getDisplayName() + ": " + ChatColor.WHITE + message);
        }
    }

    public static void sendDiscordMessage(Channel channel, Message message) {
        switch (channel) {
            case GLOBAL -> StorageFactory.globalChannel.sendMessage(message).complete();
            case STAFF -> {
                if (StorageFactory.LOGGING_ENABLED) StorageFactory.logChannel.sendMessage(message).complete();
            }
        }
    }

    public static void sendDiscordMessage(Channel channel, MessageEmbed message) {
        switch (channel) {
            case GLOBAL -> StorageFactory.globalChannel.sendMessage(message).complete();
            case STAFF -> {
                if (StorageFactory.LOGGING_ENABLED) StorageFactory.logChannel.sendMessage(message).complete();
            }
        }
    }

    public static void sendDiscordMessage(Channel channel, String message) {
        switch (channel) {
            case GLOBAL -> StorageFactory.globalChannel.sendMessage(message).complete();
            case STAFF -> {
                if (StorageFactory.LOGGING_ENABLED) StorageFactory.logChannel.sendMessage(message).complete();
            }
        }
    }

    public static void sendDiscordWebhook(Channel channel, String username, String content, String avatar_url) {
        Main.getPlugin.getLogger().log(Level.INFO, "Player " + username + " sent message: \"" + content + "\" in Channel " + channel.name());

        String url;
        if (channel == Channel.GLOBAL) {
            url = StorageFactory.WEBHOOK_URL;
        } else {
            url = StorageFactory.LOG_WEBHOOK_URL;
        }

        //TODO: handle channel

        HttpsURLConnection httpClient;
        try {
            httpClient = (HttpsURLConnection) new URL(url).openConnection();

            //add reuqest header
            httpClient.setRequestMethod("POST");
            httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String urlParameters = "username=" + username + "&content=" + content + "&avatar_url=" + avatar_url;

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

    private static Player player = null;
    private static String content = "";
    private static String advancement = "";

    public static void sendDiscordEmbed(Channel channel, Object... entries) throws ClassNotFoundException {
        parseEntries(entries);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(EmbedStyle.getColor(Class.forName(Thread.currentThread().getStackTrace()[2].getClassName())));
        eb.setFooter((player != null ? ChatColor.stripColor(player.getDisplayName()) + " " : "") + content + advancement, (player != null ? StorageFactory.AVATAR_PROVIDER_URL + player.getUniqueId() + "?size=128&overlay=true" : "https://i.imgur.com/ahJ2DJ8.png"));

        sendDiscordMessage(channel, new MessageBuilder().setEmbed(eb.build()).build());
        reset();
    }

    public static void sendDiscordEmbed(@NotNull EmbedStyle style, Channel channel, Object... entries) {
        parseEntries(entries);

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(style.getColor());
        eb.setFooter((player != null ? ChatColor.stripColor(player.getDisplayName()) + " " : "") + content + advancement, (player != null ? StorageFactory.AVATAR_PROVIDER_URL + player.getUniqueId() + "?size=128&overlay=true" : "https://i.imgur.com/ahJ2DJ8.png"));

        sendDiscordMessage(channel, new MessageBuilder().setEmbed(eb.build()).build());
        reset();
    }

    private static void reset() {
        player = null;
        content = "";
        advancement = "";
    }

    private static void parseEntries(Object @NotNull ... entries) {
        try {
            if (entries[0] != null) {
                player = (Player) entries[0];
            }
            if (entries[1] != null) {
                content = (String) entries[1];
            }
            if (entries[2] != null) {
                advancement = parseMinecraftAdvancement((Advancement) entries[2]);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private static @NotNull String parseMinecraftAdvancement(@NotNull Advancement advancement) {
        return advancement.getKey().toString().replace(
                advancement.getKey().toString().startsWith("minecraft:story") ? "minecraft:story/" :
                        advancement.getKey().toString().startsWith("minecraft:nether") ? "minecraft:nether/" :
                                advancement.getKey().toString().startsWith("minecraft:end") ? "minecraft:end/" :
                                        advancement.getKey().toString().startsWith("minecraft:adventure") ? "minecraft:adventure/" :
                                                advancement.getKey().toString().startsWith("minecraft:husbandry") ? "minecraft:husbandry/" :
                                                        "", "").replaceAll("_", " ");
    }

    public enum EmbedStyle {
        COLOR_RED(new Color(252, 83, 86)),
        COLOR_GREEN(new Color(83, 252, 100)),
        COLOR_BLUE(new Color(83, 156, 252)),
        COLOR_PINK(new Color(252, 83, 252)),
        COLOR_DEFAULT(new Color(255, 255, 255));

        private final Color color;

        EmbedStyle(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public static Color getColor(Class clazz) {
            return
                    PlayerAdvanceListener.class.equals(clazz) ? COLOR_BLUE.color :
                            PlayerDeathListener.class.equals(clazz) ? COLOR_RED.color :
                                    PlayerJoinListener.class.equals(clazz) ? COLOR_GREEN.color :
                                            PlayerLeaveListener.class.equals(clazz) ? COLOR_RED.color :
                                                    ShowItemCommand.class.equals(clazz) ? COLOR_PINK.color :
                                                            COLOR_DEFAULT.color;


        }
    }

}
