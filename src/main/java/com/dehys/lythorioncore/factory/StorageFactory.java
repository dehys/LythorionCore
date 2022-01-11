package com.dehys.lythorioncore.factory;

import com.dehys.lythorioncore.object.claim.Region;
import com.dehys.lythorioncore.object.storage.Storage;
import com.dehys.lythorioncore.object.tag.Tag;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StorageFactory {

    //claims
    public static List<Region> regions = new ArrayList<>();

    //tags
    public static List<Tag> tags = new ArrayList<>();
    public static List<Team> teams = new ArrayList<>();

    //staffchat
    public static ArrayList<UUID> staffChat = new ArrayList<>();

    public static Storage discordStorage;
    public static Storage claimsStorage;
    public static Storage tagsStorage;

    //discord.yml
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
