package com.dehys.lythorioncore.bukkit.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class ShowItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player p){
            PlayerInventory inv = p.getInventory();
            ItemStack itemStack = inv.getItem(inv.getHeldItemSlot());

            if (itemStack != null && itemStack.getType() != Material.AIR){
                int amount = itemStack.getAmount();
                Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();

                String name = "NULL";
                String rawName = "NULL";

                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
                    if (itemStack.getItemMeta().hasDisplayName()){
                        name = itemStack.getItemMeta().getDisplayName();
                        rawName = itemStack.getItemMeta().getDisplayName();
                    }else {
                        name = prettify(itemStack.getType().name().replaceAll("_", " ").trim());
                        rawName = itemStack.getType().name();
                    }
                }else{
                    name = prettify(itemStack.getType().name().replaceAll("_", " ").trim());
                    rawName = itemStack.getType().name();
                }

                if (!(amount <= 0)){

                    int value = 0;
                    for (ItemStack is : inv.getContents()){
                        String is_name = "";
                        if (is != null){
                            if (is.getType() == itemStack.getType()){
                                if (is.hasItemMeta()){
                                    if (is.getItemMeta().hasDisplayName()){
                                        is_name = is.getItemMeta().getDisplayName();
                                    }else{
                                        is_name = is.getType().name();
                                    }
                                } else{
                                    is_name = is.getType().name();
                                }

                                if (is_name.equalsIgnoreCase(rawName)){
                                    value += is.getAmount();
                                }
                            }
                        }
                    }

                    if (enchantments.isEmpty()) enchantments=null;
                    StringBuilder enchantmentsString = new StringBuilder();
                    if (enchantments != null){
                        int count = 0;
                        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()){
                            Enchantment enchantment = entry.getKey();
                            int thiccness = entry.getValue();
                            enchantmentsString
                                    .append(prettify(enchantment.getKey().toString().replaceAll("_", "").toLowerCase(Locale.US).replaceAll("minecraft:", "").trim()))
                                    .append(" ")
                                    .append(thiccness)
                                    .append(enchantments.size() == count ? "" : "\n");
                            count++;
                        }
                    }

                    String valueString = value == 1 ? "" : " x"+value;

                    TextComponent message = new TextComponent(ChatColor.GRAY+p.getDisplayName()+ChatColor.LIGHT_PURPLE+" is showing off item: ");
                    TextComponent item = new TextComponent(ChatColor.DARK_GRAY+"["+ChatColor.WHITE+name+valueString+ChatColor.DARK_GRAY+"]");
                    item.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(name+(enchantmentsString.toString().equalsIgnoreCase("") ? "" : "\n")+ChatColor.AQUA+enchantmentsString.toString().trim())));
                    message.addExtra(item);
                    for (Player player : Bukkit.getOnlinePlayers()){
                        player.spigot().sendMessage(ChatMessageType.CHAT, message);
                    }

                }else{
                    System.out.println("[SI COMMAND] AMOUNT IS WRAUUNGGG");
                }
            }else {
                System.out.println("[SI COMMAND] Item is null or AIR");
            }
        }

        return true;
    }

    public static String prettify(String s){
        String[] strs = s.toLowerCase(Locale.US).split(" ");
        StringBuilder cap = new StringBuilder();
        for (String str : strs){
            System.out.println("STR: "+str);
            cap
                    .append(str.substring(0, 1).toUpperCase())
                    .append(str.substring(1))
                    .append(" ");
        }
        return cap.toString().trim();
    }
}
