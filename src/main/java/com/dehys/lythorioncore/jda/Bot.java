package com.dehys.lythorioncore.jda;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.commands.CommandManager;
import com.dehys.lythorioncore.jda.commands.JDACommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.logging.Level;

public class Bot {
    private static final CommandManager commandManager = new CommandManager();
    private final String token;

    public static JDA jda = null;

    public Bot(String token) {
        this.token = token;
        initJDA();
    }

    private void initJDA() {
        try {
            jda = setupJDA().build();
            jda.awaitReady();
        } catch (LoginException e) {
            Main.getPlugin.getLogger().log(Level.SEVERE, "Bot token cannot be null! Please set it in the configuration.yml file and reload the server.");
            Main.unload();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StorageFactory.guild = jda.getGuildById(StorageFactory.GUILD_ID);
        if (StorageFactory.guild == null) {
            Main.getPlugin.getLogger().log(Level.SEVERE, "Guild ID cannot be null! Please set it in the configuration.yml file and reload the server.");
            Main.unload();
        }
        StorageFactory.globalChannel = StorageFactory.guild.getTextChannelById(StorageFactory.CHANNEL_ID);
        StorageFactory.logChannel = StorageFactory.guild.getTextChannelById(StorageFactory.LOG_CHANNEL_ID);
    }

    private JDABuilder setupJDA(){
        return JDABuilder.create(this.token, getIntents());
    }

    private EnumSet<GatewayIntent> getIntents(){
        return EnumSet.of(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_WEBHOOKS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.DIRECT_MESSAGES
        );
    }

    public void registerCommands(JDACommand... commands){
        for (JDACommand cmd : commands) commandManager.addCommand(cmd);
    }

    public void unregisterCommand(JDACommand... commands){
        for (JDACommand cmd : commands) commandManager.removeCommand(cmd);
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public HashSet<JDACommand> getCommands() {
        return commandManager.getCommands();
    }

    public void shutdown() {
        jda.shutdownNow();
        Bukkit.getLogger().log(Level.INFO, "[Lythorion] Waiting for JDA to shutdown...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}