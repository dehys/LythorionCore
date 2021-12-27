package com.dehys.lythorioncore.jda;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.factories.StorageFactory;
import com.dehys.lythorioncore.jda.commands.JDACommand;
import com.dehys.lythorioncore.jda.commands.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;
import java.util.HashSet;

public class Bot {
    private static final CommandManager commandManager = new CommandManager();
    private final String token;

    public static Guild guild;
    public static TextChannel channel;

    private JDA jda;

    public Bot(String token){
        this.token = token;
        try {
            initJDA();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initJDA() throws LoginException, InterruptedException {
        jda = setupJDA().build();
        jda.awaitReady();

        guild = jda.getGuildById(StorageFactory.GUILD_ID);
        assert guild != null;
        channel = guild.getTextChannelById(StorageFactory.CHANNEL_ID);
    }

    private JDABuilder setupJDA(){
        if (this.token == null){
            Main.unload();
            throw new IllegalArgumentException("Token cannot be null, please provide a token inside of configuration.yml");
        }
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

    public JDA getJDA(){
        return this.jda;
    }

    public void registerCommands(JDACommand... commands){
        for (JDACommand cmd : commands) commandManager.addCommand(cmd);
    }

    public void unregisterCommand(JDACommand... commands){
        for (JDACommand cmd : commands) commandManager.removeCommand(cmd);
    }

    public static CommandManager getCommandManager(){
        return commandManager;
    }

    public HashSet<JDACommand> getCommands() {
        return commandManager.getCommands();
    }
}