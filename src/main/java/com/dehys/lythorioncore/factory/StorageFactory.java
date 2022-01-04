package com.dehys.lythorioncore.factory;

import com.dehys.lythorioncore.object.claim.Region;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageFactory {

    //claims
    public static List<Region> regions = new ArrayList<>();

    //staffchat
    public static ArrayList<UUID> staffChat = new ArrayList<>();

    //configuration files
    public static FileConfiguration configuration;
    public static FileConfiguration claims;

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
