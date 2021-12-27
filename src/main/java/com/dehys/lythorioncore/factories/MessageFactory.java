package com.dehys.lythorioncore.factories;

import org.bukkit.plugin.java.JavaPlugin;

public class MessageFactory {

    private final JavaPlugin plugin;
    private String s1;

    public MessageFactory(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    MessageFactory(String s1) {
        this.plugin = null;
        this.s1 = s1;
    }


    public static class lythorion {
        static class status {
            public static MessageFactory online = new MessageFactory("Server online");
            public static MessageFactory offline = new MessageFactory("Server offline");
            public static MessageFactory restarting = new MessageFactory("Server restarting");
            public static MessageFactory restarted = new MessageFactory("Server restarted");
            public static MessageFactory reloading = new MessageFactory("Server reloading");
            public static MessageFactory reloaded = new MessageFactory("Server reloaded");
        }
    }

}
