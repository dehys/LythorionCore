package com.dehys.lythorioncore.object.claim;

import org.bukkit.OfflinePlayer;

public class RegionMember {

    private final Region region;
    private final OfflinePlayer player;
    private Region.RegionRank regionRank;

    public RegionMember(Region region, OfflinePlayer player, Region.RegionRank regionRank) {
        this.region = region;
        this.player = player;
        this.regionRank = regionRank;
    }

    public Region getRegion() {
        return region;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public Region.RegionRank getRegionRank() {
        return regionRank;
    }

    public void setRegionRank(Region.RegionRank regionRank) {
        this.regionRank = regionRank;
    }
}
