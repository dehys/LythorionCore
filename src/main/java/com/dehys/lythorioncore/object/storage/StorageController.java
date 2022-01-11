package com.dehys.lythorioncore.object.storage;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.factory.StorageFactory;
import com.dehys.lythorioncore.object.tag.Tag;
import com.dehys.lythorioncore.object.tag.TagManager;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class StorageController {

    public static File defaultsFolder;

    public static void setValues() {
        //Discord configuration
        StorageFactory.BOT_TOKEN = StorageFactory.discordStorage.getYaml().getString("bot-token");
        StorageFactory.GUILD_ID = StorageFactory.discordStorage.getYaml().getString("guild-id");
        StorageFactory.CHANNEL_ID = StorageFactory.discordStorage.getYaml().getString("channel-id");
        StorageFactory.WEBHOOK_URL = StorageFactory.discordStorage.getYaml().getString("webhook-url");
        StorageFactory.LOGGING_ENABLED = StorageFactory.discordStorage.getYaml().getBoolean("logging-enabled");
        StorageFactory.LOG_CHANNEL_ID = StorageFactory.discordStorage.getYaml().getString("log-channel-id");
        StorageFactory.LOG_WEBHOOK_URL = StorageFactory.discordStorage.getYaml().getString("log-webhook-url");
        StorageFactory.DISCORD_PREFIX = StorageFactory.discordStorage.getYaml().getString("discord-prefix");
        StorageFactory.AVATAR_PROVIDER_URL = StorageFactory.discordStorage.getYaml().getString("avatar-provider-url");

        //Claims data
        //TODO: Add claims data

        //Tags data
        TagManager.load();
    }

    public static void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }

    public static YamlConfiguration getDefaultStorage(String filename) throws IOException {
        String displayname = filename.replace(".yml", "");

        InputStream in = Main.plugin.getResource(filename);
        if (in == null) {
            Main.plugin.getLogger().log(Level.SEVERE, "Could not find '" + filename + "' in jar! Report this to the developers.");
            return null;
        }

        Path defFile = Paths.get(defaultsFolder.getPath() + File.separator + displayname + "_def.yml");
        Files.copy(in, defFile, StandardCopyOption.REPLACE_EXISTING);
        if (!defFile.toFile().exists()){
            if (defFile.toFile().createNewFile()) {
                Main.plugin.getLogger().log(Level.INFO, "Default file created with name: " + displayname + "_def");
            }else {
                Main.plugin.getLogger().log(Level.SEVERE, "Could not create default file with name: " + displayname + "_def");
            }
        }

        return YamlConfiguration.loadConfiguration(defFile.toFile());
    }

    static Gson makeJSON(String name) {
        File file = new File(Main.plugin.getDataFolder(), name + ".json");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    Main.plugin.getLogger().log(Level.INFO, "JSON file created with name: " + name);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

     static FileConfiguration makeYaml(String name, File file){
        if (!file.exists()){
            try{
                if (file.createNewFile()) {
                    Main.plugin.getLogger().log(Level.INFO, "YAML file created with name: " + name + ".yml");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);

        try {
            fileConfiguration.setDefaults(Objects.requireNonNull(getDefaultStorage(name + ".yml")));
            System.out.println("File configuration loaded: " + name + ".yml");
            fileConfiguration.options().copyDefaults(true);
            fileConfiguration.save(file);
            return fileConfiguration;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void createDataFolder() {
        boolean status = Main.plugin.getDataFolder().mkdir();
        if(!status) Main.plugin.getLogger().log(Level.SEVERE, "Data folder failed to create! Please check directory permissions and try again.");
        else Main.plugin.getLogger().log(Level.INFO, "Data folder created!");

    }
}
