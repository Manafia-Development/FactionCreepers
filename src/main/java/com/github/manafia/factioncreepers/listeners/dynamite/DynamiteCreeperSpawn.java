package com.github.manafia.factioncreepers.listeners.dynamite;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class DynamiteCreeperSpawn implements Listener {

    @EventHandler
    public void onCreeperSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() != EntityType.CREEPER) {
            return;
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {
            if (entity.getCustomName() == null) {
                return;
            }
            if (Enable.pl().color(Storage.DynamiteConfiguration.getString("Item.Name")).equals(entity.getCustomName())) {
                ConfigurationSection dynamiteConfiguration = Storage.DynamiteConfiguration;
                Enable.pl().applyNametag(entity, dynamiteConfiguration.getString("Nametag.Format"), dynamiteConfiguration.getBoolean("Nametag.Visible"));
            }
        }, 3L);


    }


}
