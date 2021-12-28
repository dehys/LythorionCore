package com.dehys.lythorioncore.jda.commands;


import com.dehys.lythorioncore.factories.StorageFactory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager extends ListenerAdapter {

    public static List<JDACommand> commands = new ArrayList<>();
    private final Set<Long> adminOverrides = new HashSet<>();

    public void addCommand(JDACommand command){
        commands.add(command);
    }

    public void removeCommand(String commandName){
        commands.stream().filter(command -> command.getName().equalsIgnoreCase(commandName)).forEach(commands::remove);
    }

    public void removeCommand(JDACommand command){
        commands.remove(command);
    }

    public HashSet<JDACommand> getCommands(){
        return new HashSet<>(commands);
    }

    private JDACommand getCommand(String name){
        return commands.stream().filter(command -> command.getName().equalsIgnoreCase(name) || command.getAlias().contains(name.toLowerCase())).findFirst().orElse(null);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event){

        JDACommand command = getValidCommand(event.getAuthor(), event.getMessage());

        if(command == null) return;

        if(adminOverrides.contains(event.getAuthor().getIdLong())){
            command.execute(new CommandInformation(event, event.getMessage().getContentRaw()));
            return;
        }

        if(!(Objects.requireNonNull(event.getMember()).hasPermission(command.getRequiredPermissions()))){
            event.getChannel().sendMessage("You do not have permission to use this command.").queue();
            return;
        }

        if(!(Objects.requireNonNull(event.getGuild().getSelfMember()).hasPermission(command.getRequiredPermissions()))){
            event.getChannel().sendMessage("You do not have permission to use this command.").queue();
            return;
        }
        command.execute(new CommandInformation(event, event.getMessage().getContentRaw()));
    }

    @Override
    public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
        JDACommand command = getValidCommand(event.getAuthor(), event.getMessage());
        if(command == null) return;
        if(adminOverrides.contains(event.getAuthor().getIdLong())){
            command.execute(new CommandInformation(event, event.getMessage().getContentRaw()));
            return;
        }
        command.execute(new CommandInformation(event, event.getMessage().getContentRaw()));
    }

    public JDACommand getValidCommand(User user, Message message) {
        if(user.isBot()) return null;
        String prefix = StorageFactory.DISCORD_PREFIX;
        if (!message.getContentRaw().startsWith(prefix)) {
            return null;
        }
        String[] split = message.getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        return getCommand(split[0]);
    }

    public static boolean isCommand(String s) {
        if (s.startsWith(StorageFactory.DISCORD_PREFIX)) {
            String ss = s.replaceFirst("(?i)" + Pattern.quote(StorageFactory.DISCORD_PREFIX), "");
            return commands.stream().anyMatch(command -> command.getAlias().contains(ss.split("\\s+")[0].toLowerCase()) || command.getName().equalsIgnoreCase(ss.split("\\s+")[0].toLowerCase()));
        }
        return false;
    }

    public void addAdminOverride(long id) {
        adminOverrides.add(id);
    }

    public void removeAdminOverride(long id) {
        adminOverrides.remove(id);
    }
}