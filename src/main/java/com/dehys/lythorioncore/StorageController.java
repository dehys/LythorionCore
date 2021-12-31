package com.dehys.lythorioncore;

import com.dehys.lythorioncore.factory.StorageFactory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.logging.Level;

public class StorageController {

    //storages
    private static FileConfiguration configuration;
    private static FileConfiguration claims;

    private static File defaultsFolder;
    private static JavaPlugin plugin;

    public static void initializeStorage(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
        defaultsFolder = new File(plugin.getDataFolder() + File.separator + "defaults");

        if (!plugin.getDataFolder().exists()) createDataFolder();
        if (!defaultsFolder.exists()) defaultsFolder.mkdir();

        configuration = makeYAML("configuration");
        claims = makeYAML("claims");

        try {
            configuration.setDefaults(Objects.requireNonNull(getDefaultStorage("configuration.yml")));
            claims.setDefaults(Objects.requireNonNull(getDefaultStorage("claims.yml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        configuration.options().copyDefaults(true);
        claims.options().copyDefaults(true);

        try {
            configuration.save(new File(plugin.getDataFolder(), "configuration.yml"));
            claims.save(new File(plugin.getDataFolder(), "claims.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (deleteDirectory(defaultsFolder)) {
            plugin.getLogger().log(Level.INFO, "Default files deleted, everything is fine.");
        }

        initializeValues();
    }

    private static void initializeValues() {
        StorageFactory.configuration = configuration;
        StorageFactory.claims = claims;

        StorageFactory.BOT_TOKEN = configuration.getString("bot-token");
        StorageFactory.GUILD_ID = configuration.getString("guild-id");
        StorageFactory.CHANNEL_ID = configuration.getString("channel-id");
        StorageFactory.WEBHOOK_URL = configuration.getString("webhook-url");
        StorageFactory.LOGGING_ENABLED = configuration.getBoolean("logging-enabled");
        StorageFactory.LOG_CHANNEL_ID = configuration.getString("log-channel-id");
        StorageFactory.LOG_WEBHOOK_URL = configuration.getString("log-webhook-url");
        StorageFactory.DISCORD_PREFIX = configuration.getString("discord-prefix");
        StorageFactory.AVATAR_PROVIDER_URL = configuration.getString("avatar-provider-url");
    }

    private static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private static YamlConfiguration getDefaultStorage(String filename) throws IOException {
        String displayname = filename.replace(".yml", "");

        InputStream in = plugin.getResource(filename);
        if (in == null) {
            plugin.getLogger().log(Level.SEVERE, "Could not find " + filename + " in jar! Report this to the developers.");
            return null;
        }

        Path defFile = Paths.get(defaultsFolder.getPath() + File.separator + displayname + "_def.yml");
        Files.copy(in, defFile, StandardCopyOption.REPLACE_EXISTING);
        if (!defFile.toFile().exists()){
            if (defFile.toFile().createNewFile()) {
                plugin.getLogger().log(Level.INFO, "Default file created with name: " + displayname + "_def");
            }else {
                plugin.getLogger().log(Level.SEVERE, "Could not create default file with name: " + displayname + "_def");
            }
        }

        return YamlConfiguration.loadConfiguration(defFile.toFile());
    }

    private void makeJSON(String name) {
        File file = new File(plugin.getDataFolder(), name + ".json");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    plugin.getLogger().log(Level.INFO, "JSON file created with name: " + name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static FileConfiguration makeYAML(String name){
        File file = new File(plugin.getDataFolder(), name + ".yml");
        if (!file.exists()){
            try{
                if (file.createNewFile()) {
                    plugin.getLogger().log(Level.INFO, "YAML file created with name: " + name);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    private static void createDataFolder() {
        plugin.getLogger().log(Level.INFO, "Data folder created ?= " + plugin.getDataFolder().mkdir());
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    public FileConfiguration getClaims() {
        return claims;
    }

}
