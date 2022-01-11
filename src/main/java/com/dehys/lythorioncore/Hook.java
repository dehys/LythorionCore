package com.dehys.lythorioncore;

import com.dehys.lythorioncore.command.EmptyCommandExecutor;
import com.dehys.lythorioncore.command.GenericCommand;
import com.dehys.lythorioncore.command.moderation.ReloadCommand;
import com.dehys.lythorioncore.command.moderation.RestartCommand;
import com.dehys.lythorioncore.command.moderation.StaffChatCommand;
import com.dehys.lythorioncore.command.normal.*;
import com.dehys.lythorioncore.factory.StorageFactory;
import com.dehys.lythorioncore.listener.bukkit.*;
import com.dehys.lythorioncore.listener.discord.DiscordChatListener;
import com.dehys.lythorioncore.object.storage.Storage;
import com.dehys.lythorioncore.object.storage.StorageController;
import com.dehys.lythorioncore.object.tag.Tag;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hook implements CommandExecutor {

    private final JavaPlugin plugin;

    public Hook(JavaPlugin plguin) {
        this.plugin = plguin;
    }

    public void hookStorages() {
        //Setup storage
        StorageController.defaultsFolder = new File(Main.plugin.getDataFolder() + File.separator + "defaults");
        if (!Main.plugin.getDataFolder().exists()) StorageController.createDataFolder();
        if (!StorageController.defaultsFolder.exists()) StorageController.defaultsFolder.mkdir();

        //Load storages
        StorageFactory.discordStorage = new Storage("discord", Storage.StorageType.YAML).create();
        StorageFactory.claimsStorage = new Storage("claims", Storage.StorageType.YAML).create();
        StorageFactory.tagsStorage = new Storage("tags", Storage.StorageType.YAML).create();

        //Delete temporary directory
        StorageController.deleteDirectory(StorageController.defaultsFolder);

        //initialize values
        StorageController.setValues();
    }

    public void hookCommands() {
        if (Bot.jda == null) return;

        //NON-SLASH SUPPORTIVE COMMANDS
        new StaffChatCommand();
        new ClaimCommand();
        new ShowItemCommand();
        new TagCommand();

        //SLASH SUPPORTIVE COMMANDS
        Guild guild = Main.bot.getGuild();
        guild.updateCommands().addCommands(Stream.of(
                new ReloadCommand().getCommandData(),
                new RestartCommand().getCommandData(),
                new NickCommand().getCommandData(),
                new PlayersCommand().getCommandData()
        ).flatMap(Collection::stream).collect(Collectors.toList())).complete();

        //bukkit stuff :)
        for (GenericCommand gc : Main.commandHandler.getCommands()) {
            PluginCommand pc = plugin.getCommand(gc.getName());
            if (pc != null){
                pc.setExecutor(this);
            }
        }
    }

    public void hookListeners() {
        if (Bot.jda == null) return;

        //CommandHandler
        /*  Bukkit  */
        plugin.getServer().getPluginManager().registerEvents(Main.commandHandler, plugin);
        /*  JDA     */
        Bot.jda.addEventListener(Main.commandHandler);

        //Bukkit
        plugin.getServer().getPluginManager().registerEvents(new BukkitChatListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerAdvanceListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerRenameItemListener(), plugin);


        //JDA
        Bot.jda.addEventListener(new DiscordChatListener());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return true;
    }
}
