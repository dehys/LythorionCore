package com.dehys.lythorioncore.bukkit.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class PlayerRenameItemListener implements Listener {

    @EventHandler
    public void onPlayerRenameItem(PrepareAnvilEvent e){
        if(e.getResult() != null && e.getResult().hasItemMeta() && !Objects.equals(e.getInventory().getRenameText(), "")){
            ItemStack result = e.getResult();
            ItemMeta resultMeta = result.getItemMeta();
            String nameColored = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(e.getInventory().getRenameText()));
            assert resultMeta != null;
            resultMeta.setDisplayName(nameColored);
            result.setItemMeta(resultMeta);
        }
    }
}
