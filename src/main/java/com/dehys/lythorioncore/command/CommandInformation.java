package com.dehys.lythorioncore.command;

public class CommandInformation {

    private final Object eventObject;
    private final CommandCaller commandCaller;

    public CommandInformation(CommandCaller commandCaller) {
        this.commandCaller = commandCaller;
        this.eventObject = commandCaller.eventObject;
    }

    public CommandCaller getCaller() {
        return commandCaller;
    }

    public Object getEventObject() {
        return eventObject;
    }
}