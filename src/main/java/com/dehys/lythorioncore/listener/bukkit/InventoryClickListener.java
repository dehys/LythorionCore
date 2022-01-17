package com.dehys.lythorioncore.listener.bukkit;

import com.dehys.lythorioncore.core.Main;
import com.dehys.lythorioncore.object.tag.TagManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();

        if (event.getView().getTitle().startsWith(ChatColor.LIGHT_PURPLE+"Tags Menu" + ChatColor.GRAY + " | ")) {
            event.setCancelled(true);
            if (
                    currentItem != null &&
                    currentItem.hasItemMeta() &&
                    currentItem.getType() != Material.AIR &&
                    Objects.requireNonNull(currentItem.getItemMeta()).hasDisplayName()
            )
            {
                TagManager.toggle(currentItem.getItemMeta().getDisplayName(), player);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                new BukkitRunnable(){
                    final Location loc = player.getLocation();
                    double t = 0;
                    final double r = 1;
                    public void run(){
                        t = t + Math.PI/16;
                        double x = r*cos(t);
                        double y = r*sin(t);
                        double z = r*sin(t);
                        loc.add(x, y, z);
                        player.getWorld().spawnParticle(Particle.CLOUD, loc, 0, 0, 0, 0, 1);
                        loc.subtract(x, y, z);
                        if (t > Math.PI*8){
                            this.cancel();
                        }
                    }
                }.runTaskTimer(Main.plugin, 0, 1);
                event.getView().close();
                player.openInventory(TagManager.getMenu(player));
            }
        }
    }
}
