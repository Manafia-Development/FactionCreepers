package com.github.manafia.factioncreepers.listeners;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ThrowableCegg implements Listener {

    @EventHandler
    public void cegg(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player.getItemInHand().getType() != Material.MONSTER_EGG
                || e.getAction() != Action.LEFT_CLICK_AIR) {
            return;
        }

        //   if (player.getItemInHand().getItemMeta())

        ItemStack playerItemStack = player.getItemInHand();

        if (((playerItemStack.getItemMeta() == null || playerItemStack.getItemMeta().getDisplayName() == null) && !Storage.ThrowableCreeperEggs.getBoolean("Normal"))
                || (Enable.pl().color(Storage.RocketConfiguration.getString("Item.Name")).equalsIgnoreCase(playerItemStack.getItemMeta().getDisplayName()) && !Storage.ThrowableCreeperEggs.getBoolean("Rocket"))
                || (Enable.pl().color(Storage.DynamiteConfiguration.getString("Item.Name")).equalsIgnoreCase(playerItemStack.getItemMeta().getDisplayName()) && !Storage.ThrowableCreeperEggs.getBoolean("Dynamite"))
                || (Enable.pl().color(Storage.SandstormConfiguration.getString("Item.Name")).equalsIgnoreCase(playerItemStack.getItemMeta().getDisplayName()) && !Storage.ThrowableCreeperEggs.getBoolean("Sandstorm"))
                || (Enable.pl().color(Storage.StickyConfiguration.getString("Item.Name")).equalsIgnoreCase(playerItemStack.getItemMeta().getDisplayName()) && !Storage.ThrowableCreeperEggs.getBoolean("Sticky"))) {
            return;
        }


        final Item cegg = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(Material.MONSTER_EGG, 1, (short) 50));
        cegg.setVelocity(player.getEyeLocation().getDirection());

        if (playerItemStack.getAmount() == 1) {
            player.setItemInHand(new ItemStack(Material.AIR));
        } else {
            playerItemStack.setAmount(playerItemStack.getAmount() - 1);
            player.setItemInHand(playerItemStack);
        }


        final String itemName;
        if (playerItemStack.getItemMeta() == null || playerItemStack.getItemMeta().getDisplayName() == null) {
            itemName = null;
        } else {
            itemName = playerItemStack.getItemMeta().getDisplayName();
        }
        Enable.pl().getServer().getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {
            if (itemName == null) {
                // this means it was a normal c egg
                cegg.getWorld().spawnEntity(cegg.getLocation(), EntityType.CREEPER);
                return;
            }

            boolean stickyCreeper = false;

            String tag = null;
            if (Enable.pl().color(Storage.RocketConfiguration.getString("Item.Name")).equalsIgnoreCase(itemName)) {
                tag = Storage.RocketConfiguration.getString("Nametag.Format");
            } else if (Enable.pl().color(Storage.DynamiteConfiguration.getString("Item.Name")).equalsIgnoreCase(itemName)) {
                tag = Storage.DynamiteConfiguration.getString("Nametag.Format");
            } else if (Enable.pl().color(Storage.SandstormConfiguration.getString("Item.Name")).equalsIgnoreCase(itemName)) {
                tag = Storage.SandstormConfiguration.getString("Nametag.Format");
            } else if (Enable.pl().color(Storage.StickyConfiguration.getString("Item.Name")).equalsIgnoreCase(itemName)) {
                tag = Storage.StickyConfiguration.getString("Nametag.Format");
                stickyCreeper = true;
            }
            cegg.setPickupDelay(Integer.MAX_VALUE);
            if (tag != null) {
                // this means there is a custom c egg
                Entity creeper = cegg.getWorld().spawnEntity(cegg.getLocation(), EntityType.CREEPER);
                creeper.setCustomName(Enable.pl().color(tag));

                if (!stickyCreeper) {
                    // Dont apply nametag to stickycreepers because they have a special armorstand for this purpose.
                    Enable.pl().applyNametag(creeper, tag, true);
                }


                cegg.remove();
            } else {
                // this means it was a normal c egg with some custom name.
                cegg.getWorld().spawnEntity(cegg.getLocation(), EntityType.CREEPER);
                cegg.remove();
            }


        }, 20L);
    }


}
