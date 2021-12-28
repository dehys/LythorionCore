package com.dehys.lythorioncore;

import com.dehys.lythorioncore.factories.MessageFactory;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.Bot;
import com.dehys.lythorioncore.jda.features.DescriptionUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Bot discordBot;
    public static Main getPlugin;
    public static Hook hook;

    @Override
    public void onEnable() {

        //setup storage
        StorageController.initializeStorage(this);

        //initialize objects
        getPlugin = this;
        discordBot = new Bot(StorageFactory.BOT_TOKEN);

        if (Bot.jda != null) {
            hook = new Hook(this, discordBot);

            //register event handlers and commands
            hook.hookListeners();
            hook.hookCommands();

            //run features
            DescriptionUpdate.run();

            //send message to discord
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_GREEN, Channel.GLOBAL, null, MessageFactory.SERVER_START.getMessage());
        }
    }

    @Override
    public void onDisable() {


        //unhook event handlers and commands
        if (hook != null) {
            hook.unhookListeners();
            hook.unhookCommands();
        }

        //shutdown discord bot and send message to discord
        if (Bot.jda != null) {
            MessageUtil.sendDiscordEmbed(MessageUtil.EmbedStyle.COLOR_RED, Channel.GLOBAL, null, MessageFactory.SERVER_STOP.getMessage());
            discordBot.shutdown();
        }
    }

    //unloads the plugin from the server
    public static void unload(){
        Bukkit.getPluginManager().disablePlugin(getPlugin);
    }
}