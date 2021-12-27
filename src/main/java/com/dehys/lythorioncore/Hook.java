package com.dehys.lythorioncore;

import com.dehys.lythorioncore.bukkit.commands.NickCommand;
import com.dehys.lythorioncore.bukkit.commands.ShowItemCommand;
import com.dehys.lythorioncore.bukkit.commands.StaffChatCommand;
import com.dehys.lythorioncore.bukkit.listeners.*;
import com.dehys.lythorioncore.jda.Bot;
import com.dehys.lythorioncore.jda.commands.JDACommand;
import com.dehys.lythorioncore.jda.commands.moderation.ReloadCommand;
import com.dehys.lythorioncore.jda.commands.moderation.RestartCommand;
import com.dehys.lythorioncore.jda.commands.utilities.PlayersCommand;
import com.dehys.lythorioncore.jda.listeners.DiscordChatListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Hook {

    private final JavaPlugin plugin;
    private final Bot bot;

    public Hook(JavaPlugin plguin, Bot bot){
        this.plugin = plguin;
        this.bot = bot;
    }

    public void hookCommands(){
        //Bukkit
        Objects.requireNonNull(plugin.getCommand("nick")).setExecutor(new NickCommand());
        Objects.requireNonNull(plugin.getCommand("showitem")).setExecutor(new ShowItemCommand());
        Objects.requireNonNull(plugin.getCommand("staffchat")).setExecutor(new StaffChatCommand());

        //JDA //TODO: store commands in a list and register them all
        bot.registerCommands(new RestartCommand());
        bot.registerCommands(new ReloadCommand());
        bot.registerCommands(new PlayersCommand());
    }

    public void unhookCommands(){
        Objects.requireNonNull(plugin.getCommand("nick")).setExecutor(null);
        Objects.requireNonNull(plugin.getCommand("showitem")).setExecutor(null);
        Objects.requireNonNull(plugin.getCommand("staffchat")).setExecutor(null);

        for (JDACommand command : bot.getCommands()) {
            bot.unregisterCommand(command);
        }
    }

    public void unhookCommand(String s1){
        Objects.requireNonNull(plugin.getCommand(s1)).setExecutor(null);
    }

    public void unhookCommand(JDACommand command){
        bot.unregisterCommand(command);
    }

    public void hookListeners(){
        //Bukkit
        plugin.getServer().getPluginManager().registerEvents(new BukkitChatListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerAdvanceListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerRenameItemListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new CommandPreprocessListener(), plugin);

        //JDA
        bot.getJDA().addEventListener(Bot.getCommandManager());
        bot.getJDA().addEventListener(new DiscordChatListener());
    }

    public void unhookListeners(){
        //Bukkit
        HandlerList.unregisterAll(plugin);

        //JDA //TODO: store listeners in a list and unregister them all
        bot.getJDA().removeEventListener(Bot.getCommandManager());
        bot.getJDA().removeEventListener(new DiscordChatListener());
    }

    public void unhookListener(Listener listener){
        HandlerList.unregisterAll(listener);
    }

    public void unhookListener(ListenerAdapter listener){
        bot.getJDA().removeEventListener(listener);
    }

}
