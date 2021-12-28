package com.dehys.lythorioncore.factories;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;

public class StorageFactory {

    public static FileConfiguration configuration;
    public static FileConfiguration claims;

    public static ArrayList<UUID> staffChat = new ArrayList<>();

    public static Guild guild;
    public static TextChannel globalChannel;
    public static TextChannel logChannel;

    //Configuration.txt
    public static String BOT_TOKEN;
    public static String GUILD_ID;
    public static String CHANNEL_ID;
    public static String WEBHOOK_URL;
    public static boolean LOGGING_ENABLED;
    public static String LOG_CHANNEL_ID;
    public static String LOG_WEBHOOK_URL;
    public static String DISCORD_PREFIX;
    public static String AVATAR_PROVIDER_URL;


}
