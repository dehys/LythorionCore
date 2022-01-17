package com.dehys.lythorioncore.command.normal;

import com.dehys.lythorioncore.core.Main;
import com.dehys.lythorioncore.command.CommandCaller;
import com.dehys.lythorioncore.command.GenericCommand;
import com.dehys.lythorioncore.object.tag.TagManager;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TagCommand implements GenericCommand {

    public TagCommand() {
        Main.commandHandler.addCommand(this);
    }

    @Override
    public @NotNull String getName() {
        return "tag";
    }

    @Override
    public @NotNull Collection<String> getAlias() {
        Set<String> aliases = new HashSet<>();
        aliases.add("tags");
        return aliases;
    }

    @Override
    public @NotNull String getHelp(CommandCaller commandCaller) {
        if (commandCaller == CommandCaller.MINECRAFT_PLAYER) return "§6/tag §7- §eOpens the tag menu";
        else return GenericCommand.super.getHelp(commandCaller);
    }

    @Override
    public void execute(PlayerCommandPreprocessEvent event) {
        //open inventory for event player
        event.getPlayer().openInventory(TagManager.getMenu(event.getPlayer()));
    }

    @Override
    public void execute(ServerCommandEvent event) {
    }

    @Override
    public void execute(MessageReceivedEvent event) {
    }

    @Override
    public void execute(SlashCommandEvent event) {
    }
}
