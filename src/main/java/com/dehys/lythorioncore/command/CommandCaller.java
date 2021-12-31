package com.dehys.lythorioncore.command;

public enum CommandCaller {
    MINECRAFT_PLAYER,
    MINECRAFT_CONSOLE,
    DISCORD_PRIVATE,
    DISCORD_TEXT,
    DISCORD_SLASH;

    public Object eventObject;

    public CommandCaller setEventObject(Object event) {
        this.eventObject = event;
        return this;
    }
}
