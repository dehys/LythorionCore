package com.dehys.lythorioncore;

import com.dehys.lythorioncore.factories.MessageFactory;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.Bot;

import com.dehys.lythorioncore.jda.features.DescriptionUpdate;
import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
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
        hook = new Hook(this, discordBot);

        //register event handlers and commands
        hook.hookListeners();
        hook.hookCommands();

        //send message to discord
        new DiscordChatListener(DiscordChatListener.ChatType.PLAYER_JOIN, null, "Server started");
        //create new instance of DescriptionUpdate
        new DescriptionUpdate();
    }

    @Override
    public void onDisable() {

        //unhook event handlers and commands
        hook.unhookListeners();
        hook.unhookCommands();

    }

    //unloads the plugin from the server
    public static void unload(){
        Bukkit.getPluginManager().disablePlugin(getPlugin);
    }
}