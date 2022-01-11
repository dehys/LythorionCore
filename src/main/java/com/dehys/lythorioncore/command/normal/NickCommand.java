package com.dehys.lythorioncore.command.normal;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NickCommand implements GenericCommand {

    public NickCommand() {
        Main.commandHandler.addCommand(this);
    }

    @Override
    public List<CommandData> getCommandData() {
        List<CommandData> data = GenericCommand.super.getCommandData();
        for (CommandData d : data) {
            d.addOption(OptionType.STRING, "name", "The desired nickname", false);
        }
        return data;
    }

    @Override
    public @NotNull String getName() {
        return "nick";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("nickname");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        if (commandCaller == CommandCaller.MINECRAFT_PLAYER) return "§6/nick <name> §7- §eChange your nickname";
        else if (commandCaller == CommandCaller.DISCORD_SLASH) return "Change your nickname on Lythorion";
        else return GenericCommand.super.getHelp(commandCaller);
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
        event.getPlayer().sendMessage(getHelp(CommandCaller.MINECRAFT_PLAYER));
    }

    @Override
    public void execute(ServerCommandEvent event) {
    }

    @Override
    public void execute(MessageReceivedEvent event) {
    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.deferReply().setEphemeral(true).complete();
        OptionMapping nameOption = event.getOption("name");

        if (nameOption == null) {
            event.getHook().sendMessage("You reset your nickname").setEphemeral(true).complete();
        } else {
            String nick = nameOption.getAsString();
            event.getHook().sendMessage("Changed your nickname to **" + nick + "**").setEphemeral(true).complete();
        }
    }
}
