package com.dehys.lythorioncore;

import com.dehys.lythorioncore.command.EmptyCommandExecutor;
import com.dehys.lythorioncore.command.moderation.ReloadCommand;
import com.dehys.lythorioncore.command.moderation.RestartCommand;
import com.dehys.lythorioncore.command.moderation.StaffChatCommand;
import com.dehys.lythorioncore.command.normal.ClaimCommand;
import com.dehys.lythorioncore.command.normal.NickCommand;
import com.dehys.lythorioncore.command.normal.PlayersCommand;
import com.dehys.lythorioncore.command.normal.ShowItemCommand;
import com.dehys.lythorioncore.listener.bukkit.*;
import com.dehys.lythorioncore.listener.discord.DiscordChatListener;
import net.dv8tion.jda.api.entities.Guild;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hook {

    private final JavaPlugin plugin;

    public Hook(JavaPlugin plguin) {
        this.plugin = plguin;
    }

    public void hookCommands() {
        if (Bot.jda == null) return;

        //Bukkit roll-around
        EmptyCommandExecutor emptyCommandExecutor = new EmptyCommandExecutor();
        Objects.requireNonNull(plugin.getCommand("reload")).setExecutor(emptyCommandExecutor);
        Objects.requireNonNull(plugin.getCommand("restart")).setExecutor(emptyCommandExecutor);
        Objects.requireNonNull(plugin.getCommand("staffchat")).setExecutor(emptyCommandExecutor);
        Objects.requireNonNull(plugin.getCommand("claim")).setExecutor(emptyCommandExecutor);
        Objects.requireNonNull(plugin.getCommand("nick")).setExecutor(emptyCommandExecutor);
        Objects.requireNonNull(plugin.getCommand("showitem")).setExecutor(emptyCommandExecutor);

        //NON-SLASH SUPPORTIVE COMMANDS
        new StaffChatCommand();
        new ClaimCommand();
        new ShowItemCommand();

        //SLASH SUPPORTIVE COMMANDS
        Guild guild = Main.getBot.getGuild();
        guild.updateCommands().addCommands(Stream.of(
                new ReloadCommand().getCommandData(),
                new RestartCommand().getCommandData(),
                new NickCommand().getCommandData(),
                new PlayersCommand().getCommandData()
        ).flatMap(Collection::stream).collect(Collectors.toList())).complete();
    }

    public void hookListeners() {
        if (Bot.jda == null) return;

        //CommandHandler
        /*  Bukkit  */
        plugin.getServer().getPluginManager().registerEvents(Main.getCommandHandler, plugin);
        /*  JDA     */
        Bot.jda.addEventListener(Main.getCommandHandler);

        //Bukkit
        plugin.getServer().getPluginManager().registerEvents(new BukkitChatListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerAdvanceListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerRenameItemListener(), plugin);


        //JDA
        Bot.jda.addEventListener(new DiscordChatListener());
    }

    public static Map<String, Command> getKnownCommands() {
        SimplePluginManager spm = (SimplePluginManager) Bukkit.getPluginManager();

        try {
            Field commandMap = SimplePluginManager.class.getDeclaredField("commandMap");
            Field knownCommands = SimpleCommandMap.class.getDeclaredField("knownCommands");

            commandMap.setAccessible(true);
            knownCommands.setAccessible(true);

            return (Map<String, Command>) knownCommands.get(commandMap.get(spm));
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
