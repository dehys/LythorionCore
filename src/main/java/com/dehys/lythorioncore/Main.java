package com.dehys.lythorioncore;

import com.dehys.lythorioncore.command.CommandHandler;
import com.dehys.lythorioncore.factory.MessageFactory;
import com.dehys.lythorioncore.factory.StorageFactory;
import com.dehys.lythorioncore.feature.DescriptionUpdate;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Bot getBot;
    public static Main getPlugin;
    public static Hook getHook;
    public static CommandHandler getCommandHandler;

    @Override
    public void onEnable() {

        //setup storage
        StorageController.initializeStorage(this);

        //initialize objects
        getPlugin = this;
        getBot = new Bot(StorageFactory.BOT_TOKEN);
        getCommandHandler = new CommandHandler();

        if (Bot.jda != null) {
            getHook = new Hook(this);

            //register event handlers and commands
            getHook.hookListeners();
            getHook.hookCommands();

            //run features
            DescriptionUpdate.run();

            //send message to discord
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_GREEN, Channel.GLOBAL, null, MessageFactory.SERVER_START.getMessage());
        }
    }

    @Override
    public void onDisable() {

        //shutdown discord bot and send message to discord
        if (!(Bot.jda.getStatus() == JDA.Status.SHUTDOWN || Bot.jda.getStatus() == JDA.Status.SHUTTING_DOWN)) {
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_RED, Channel.GLOBAL, null, MessageFactory.SERVER_STOP.getMessage());
            getBot.shutdown();
        }

    }

    //unloads the plugin from the server
    public static void unload(){
        Bukkit.getPluginManager().disablePlugin(getPlugin);
    }
}