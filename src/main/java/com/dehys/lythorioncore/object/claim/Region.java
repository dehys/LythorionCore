package com.dehys.lythorioncore.object.claim;

import com.dehys.lythorioncore.factory.StorageFactory;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.List;

public class Region {

    private final int id;
    private String name;
    private String description;
    private final RegionMember owner;
    private List<RegionMember> members;
    private String color;
    private final World world;
    private List<Chunk> chunks;

    public Region(int id, String name, String description, RegionMember owner, List<RegionMember> members, String color, World world, List<Chunk> chunks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.members = members;
        this.color = color;
        this.world = world;
        this.chunks = chunks;

        StorageFactory.regions.add(this);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RegionMember getOwner() {
        return owner;
    }

    public List<RegionMember> getMembers() {
        return members;
    }

    public void setMembers(List<RegionMember> members) {
        this.members = members;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public World getWorld() {
        return world;
    }

    public List<Chunk> getChunks() {
        return chunks;
    }

    public void setChunks(List<Chunk> chunks) {
        this.chunks = chunks;
    }

    public enum RegionRank {
        OWNER(RegionAction.values()),
        ADMIN(new RegionAction[]{
                RegionAction.CLAIM,
                RegionAction.UNCLAIM,
                RegionAction.INVITE,
                RegionAction.KICK,
                RegionAction.BUILD,
                RegionAction.DESTROY
        }),
        MEMBER(new RegionAction[]{
                RegionAction.BUILD,
                RegionAction.DESTROY
        });

        private final RegionAction[] actions;

        RegionRank(RegionAction[] actions) {
            this.actions = actions;
        }

        public RegionAction[] getActions() {
            return actions;
        }
    }

    public enum RegionAction {
        RENAME,
        CLAIM,
        UNCLAIM,
        PROMOTE,
        BUILD,
        DESTROY,
        INVITE,
        KICK
    }


}
