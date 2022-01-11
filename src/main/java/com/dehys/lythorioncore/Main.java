package com.dehys.lythorioncore;

import com.dehys.lythorioncore.command.CommandHandler;
import com.dehys.lythorioncore.factory.MessageFactory;
import com.dehys.lythorioncore.factory.StorageFactory;
import com.dehys.lythorioncore.object.storage.Storage;
import com.dehys.lythorioncore.object.storage.StorageController;
import com.dehys.lythorioncore.object.tag.TagManager;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Bot bot;
    public static Main plugin;
    public static Hook hook;
    public static CommandHandler commandHandler;


    @Override
    public void onEnable() {
        plugin = this;
        hook = new Hook(this);

        //initialize objects
        hook.hookStorages();
        bot = new Bot(StorageFactory.BOT_TOKEN);
        commandHandler = new CommandHandler();

        if (Bot.jda != null) {
            hook.hookListeners();
            hook.hookCommands();

            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_GREEN, Channel.GLOBAL, null, MessageFactory.SERVER_START.getMessage());
        }
    }

    @Override
    public void onDisable() {

        //shutdown discord bot and send message to discord
        if (Bot.jda != null) {
            if (!(Bot.jda.getStatus() == JDA.Status.SHUTDOWN || Bot.jda.getStatus() == JDA.Status.SHUTTING_DOWN)) {
                MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_RED, Channel.GLOBAL, null, MessageFactory.SERVER_STOP.getMessage());
                bot.shutdown();
            }
        }
    }

    //unloads the plugin from the server
    public static void unload(){
        Bukkit.getPluginManager().disablePlugin(plugin);
    }
}