package com.dehys.lythorioncore.object.tag;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tag implements Comparable<Tag> {

    public List<UUID> playerList;

    public String name;
    public String description;
    public String prefix;
    public String suffix;
    public int weight;
    public boolean isStaticTag;
    public String permission;

    public Tag(String name, String description, String prefix, String suffix, int weight, boolean isStaticTag, String permission) {
        this.name = name;
        this.description = description;
        this.prefix = prefix;
        this.suffix = suffix;
        this.weight = weight;
        this.isStaticTag = isStaticTag;
        this.permission = permission;

        this.playerList = new ArrayList<>();
    }

    public void addPlayer(UUID playerUUID) {
        playerList.add(playerUUID);
    }

    @Override
    public int compareTo(@NotNull Tag o) {
        return this.weight - o.weight;
    }
}
