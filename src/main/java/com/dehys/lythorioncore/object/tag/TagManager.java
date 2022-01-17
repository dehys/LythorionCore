package com.dehys.lythorioncore.object.tag;

import com.dehys.lythorioncore.core.MessageUtil;
import com.dehys.lythorioncore.factory.StorageFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TagManager {

    public static List<Tag> getTags() {
        return StorageFactory.tags;
    }

    public static String getNameOfPlayer(Player player) {
        StringBuilder prefixes = new StringBuilder();
        StringBuilder suffixes = new StringBuilder();
        List<Tag> staticTags = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();

        for (Tag tag : getTagsOfPlayer(player)) {
            if (tag.isStaticTag) {
                staticTags.add(tag);
            } else {
                if (tag.playerList.contains(player.getUniqueId())) {
                    tags.add(tag);
                }
            }
        }

        Collections.sort(staticTags);
        Collections.sort(tags);

        boolean staticPrefixes = false;
        boolean staticSuffixes = false;

        for (Tag tag : staticTags) {
            if (staticPrefixes && staticSuffixes) break;
            if (!tag.prefix.isBlank() && !tag.prefix.equalsIgnoreCase("&r") && !staticPrefixes) {
                prefixes.append(tag.prefix);
                staticPrefixes = true;
            }
            if (!tag.suffix.isBlank() && !tag.suffix.equalsIgnoreCase("&r") && !staticSuffixes) {
                suffixes.append(tag.suffix);
                staticSuffixes = true;
            }
        }

        for (Tag tag : tags) {
            if (!tag.prefix.equalsIgnoreCase("&r")){
                prefixes.append(tag.prefix);
            }
            if (!tag.suffix.equalsIgnoreCase("&r")){
                suffixes.append(tag.suffix);
            }
        }

        return prefixes.toString() + player.getName() + suffixes.toString();
    }

    public static void initializeTeams(){
        Scoreboard sb = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        sb.getTeams().forEach(Team::unregister);

        for (Tag tag : StorageFactory.tags) {
            String teamName = getLastColor(tag.prefix).toString();
            ChatColor teamColor = getLastColor(tag.prefix);

            if (tag.prefix.contains("&#") || tag.suffix.contains("&#")) {
                teamName = MessageUtil.colorize(tag.prefix);
            }

            boolean teamExists = false;

            for (Team teamList : sb.getTeams()) {
                if (teamList.getName().equalsIgnoreCase(teamName)) {
                    teamExists = true;
                }
            }

            if (!teamExists) {
                Team team = sb.registerNewTeam(teamName);
                team.setColor(teamColor);
                StorageFactory.teams.add(team);
            }

            for (Player p : Bukkit.getOnlinePlayers()) {
                reloadTeamForPlayer(p);
            }
        }
    }

    public static ChatColor getLastColor(String s) {
        String[] arr1 = s.split("&");
        List<ChatColor> colorCodes = new ArrayList<>();

        for (String str : arr1) {
            if (str != null && !str.isEmpty()) {
                colorCodes.add(ChatColor.getByChar(str.charAt(0)));
            }
        }

        ChatColor tmpColor = null;
        for (ChatColor colorCode : colorCodes) {
            if (colorCode != null && colorCode.isColor()) {
                tmpColor = colorCode;
            }
        }
        return tmpColor != null ? tmpColor : ChatColor.WHITE;
    }

    public static void reloadTeamForPlayer(Player p){
        if (p == null) return;
        String name = getNameOfPlayer(p);
        String[] arr1 = name.split(p.getName());
        if (arr1.length < 1) return;
        String teamName = getLastColor(name.split(p.getName())[0]).toString();

        for (Team team : Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeams()) {
            if (team.getEntries().contains(p.getName())) {
                team.removeEntry(Objects.requireNonNull(p.getName()));
            }
        }

        Team team = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam(teamName);
        assert team != null;
        team.addEntry(Objects.requireNonNull(p.getName()));
    }

    private static List<Tag> getTagsOfPlayer(Player player) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : getTags()) {
            if (player.hasPermission(tag.permission)) {
                tags.add(tag);
            }
        }
        return tags;
    }

    private static List<Tag> getActivatedTagsOfPlayer(Player player) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : getTagsOfPlayer(player)) {
            if (tag.playerList.contains(player.getUniqueId())){
                tags.add(tag);
            }
        }
        return tags;
    }

    public static List<Tag> getInactiveTagsOfPlayer(Player player) {
        List<Tag> tags = new ArrayList<>();
        for (Tag tag : getTagsOfPlayer(player)) {
            if (!tag.playerList.contains(player.getUniqueId())){
                tags.add(tag);
            }
        }
        return tags;
    }

    public static Inventory getMenu(Player player){
        StringBuilder activeTags = new StringBuilder();
        getActivatedTagsOfPlayer(player).forEach(tag -> activeTags.append(tag.name).append(", "));
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.LIGHT_PURPLE+"Tags Menu" + ChatColor.GRAY + " | " + activeTags);

        List<Tag> tagsList = StorageFactory.tags;
        Collections.sort(tagsList);
        List<ItemStack> items = new ArrayList<>();

        for (Tag tag : tagsList){
            if (tag != null && !tag.isStaticTag) {

                ItemStack item = new ItemStack(Material.NAME_TAG, 1);
                item.setItemMeta(getItemMetaOfTag(item, tag, player));
                items.add(item);

            }
        }

        items.forEach(inventory::addItem);
        for (int i = 0; i < items.size(); i++) {
            inventory.setItem(i, items.get(i));
        }
        return inventory;
    }

    public static ItemMeta getItemMetaOfTag(ItemStack item, Tag tag, Player player){
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(item.getType());
        }

        assert meta != null;
        meta.setDisplayName(MessageUtil.colorize(tag.name));
        List<String> baseLore = new ArrayList<>(List.of(
                ChatColor.GRAY + "" + ChatColor.ITALIC + tag.description,
                ChatColor.GRAY + "prefix: " + MessageUtil.colorize(tag.prefix) + ChatColor.RESET,
                tag.suffix == null || tag.suffix.isBlank() ? "" : ChatColor.GRAY + "suffix: " + MessageUtil.colorize(tag.suffix) + ChatColor.RESET));

        if (getActivatedTagsOfPlayer(player).contains(tag)) {

            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            baseLore.add(ChatColor.GREEN + "[Active] " + ChatColor.GRAY + "Click to deactivate");
            meta.setLore(baseLore);


        } else if (getInactiveTagsOfPlayer(player).contains(tag)) {

            baseLore.add(ChatColor.RED + "[Inactive] " + ChatColor.GRAY + "Click to activate");
            meta.setLore(baseLore);

        } else {

            meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + tag.name);
            meta.setLore(List.of("?????"));

        }

        return meta;
    }

    public static boolean activate(Tag tag, Player player){
        if (tag.playerList.contains(player.getUniqueId())) return false;
        tag.playerList.add(player.getUniqueId());
        reloadTeamForPlayer(player);

        List<String> newEnties = new ArrayList<>();
        for (String entry : StorageFactory.tagsStorage.getYaml().getStringList("players")) {
            if (!entry.startsWith(player.getUniqueId().toString())) {
                newEnties.add(entry);
            }
        }

        String newEntry = getPlayerEntry(player);

        if (!newEntry.isBlank()) {
            if (!newEntry.contains(tag.name)){
                newEntry = newEntry + ":" + tag.name;
            }

            if (newEntry.equalsIgnoreCase(player.getUniqueId().toString()) || newEntry.equalsIgnoreCase(player.getUniqueId() + ":")) {
                newEntry = "";
            }
            newEnties.add(newEntry);
        }

        StorageFactory.tagsStorage.getYaml().set("players", newEnties);
        StorageFactory.tagsStorage.save();
        return true;
    }

    public static boolean activate(String tagName, Player p){
        Optional<Tag> opTag = StorageFactory.tags.stream().filter(tag1 -> Objects.equals(tag1.name, tagName)).findFirst();
        if (opTag.isEmpty()) return false;
        Tag tag = opTag.get();
        return activate(tag, p);
    }

    public static boolean deactivate(Tag tag, Player player){
        if (!tag.playerList.contains(player.getUniqueId())) return false;
        tag.playerList.remove(player.getUniqueId());
        reloadTeamForPlayer(player);

        List<String> newEnties = new ArrayList<>();
        for (String entry : StorageFactory.tagsStorage.getYaml().getStringList("players")) {
            if (!entry.startsWith(player.getUniqueId().toString())) {
                newEnties.add(entry);
            }
        }

        String newEntry = getPlayerEntry(player);
        if (!newEntry.isBlank()) {
            newEntry = newEntry.replaceAll(":"+tag.name, "");

            if (!newEntry.equalsIgnoreCase(player.getUniqueId().toString()) || !newEntry.equalsIgnoreCase(player.getUniqueId() + ":")) {
                newEnties.add(newEntry);
            }
        }

        StorageFactory.tagsStorage.getYaml().set("players", newEnties);
        StorageFactory.tagsStorage.save();
        return true;
    }

    public static boolean deactivate(String tagName, Player p){
        Optional<Tag> opTag = StorageFactory.tags.stream().filter(tag1 -> Objects.equals(tag1.name, tagName)).findFirst();
        if (opTag.isEmpty()) return false;
        Tag tag = opTag.get();
        return deactivate(tag, p);
    }

    public static boolean toggle(String tagName, Player p){
        Optional<Tag> opTag = StorageFactory.tags.stream().filter(tag1 -> Objects.equals(tag1.name, tagName)).findFirst();
        if (opTag.isEmpty()) return false;
        Tag tag = opTag.get();
        if (tag.playerList.contains(p.getUniqueId())) return deactivate(tag, p);
        return activate(tag, p);
    }

    private static String getPlayerEntry(Player player){
        List<String> currentPlayerEntries = StorageFactory.tagsStorage.getYaml().getStringList("players");

        boolean found = false;
        StringBuilder playerEntry = new StringBuilder();

        //Finds the players entry and sets it to newPlayerEntry
        for (String entry : currentPlayerEntries) {
            if (entry.startsWith(player.getUniqueId().toString())) {
                playerEntry.append(entry);
                found = true;
                break;
            }
        }

        //If the player doesn't have an entry, create one and set it to newPlayerEntry
        if (!found) {
            return player.getUniqueId().toString();
        } else {
            //Parse the entry and get all tags and handle them accordingly
            String tagNames = playerEntry.toString().split(":", 2)[1];

            //return if the entry only has "UUID:" and no tag-names
            if (tagNames.isBlank()) {
                return player.getUniqueId().toString();
            }

            List<Tag> newTags = new ArrayList<>();
            for (String tagName : tagNames.split(":")) {
                Tag tag = getTagByName(tagName);
                if (tag != null && !tagName.isBlank() && !playerEntry.toString().contains(tag.name)) {
                    newTags.add(tag);
                }
            }

            Collections.sort(newTags); //Sort the tags by weight
            newTags.forEach(tag -> playerEntry.append(":").append(tag.name)); //Add all the tags
            return playerEntry.toString();
        }
    }

    public static void load() {
        StorageFactory.tagsStorage.getYaml().setComments("tags", List.of(
                "List of all tags",
                "Format: name:description:prefix:suffix:weight:isStaticTag:permission",
                " ",
                "Example: Admin:this is the default admin group tag:&c[Admin] &b:&r:0:true:group.admin",
                "Result: &c[Admin] &b<player_name>&r: <message> - you can see it here: <link>"
        ));
        StorageFactory.tagsStorage.getYaml().setComments("players", List.of(
                "List of players uuid with their activated tags"
        ));
        StorageFactory.tagsStorage.save();
        for (String s : StorageFactory.tagsStorage.getYaml().getStringList("tags")) {
            String[] split = s.split(":");
            StorageFactory.tags.add(new Tag(split[0], split[1], split[2], split[3], Integer.parseInt(split[4]), Boolean.parseBoolean(split[5]), split[6]));
        }
        for (String s : StorageFactory.tagsStorage.getYaml().getStringList("players")) {
            try {
                UUID uuid = UUID.fromString(s.split(":")[0]);
                String tagNames = s.split(":", 2)[1];
                for (String tagName : tagNames.split(":")) {
                    Tag tag = getTagByName(tagName);
                    if (tag != null) tag.playerList.add(uuid);
                }

            } catch (IllegalArgumentException ignored){};
        }
        initializeTeams();
    }

    public static Tag getTagByName(String tagName) {
        Optional<Tag> opTag = StorageFactory.tags.stream().filter(tag -> Objects.equals(tag.name, tagName)).findFirst();
        if (opTag.isEmpty()) return null;
        return opTag.get();
    }
}
