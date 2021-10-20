package com.github.manafia.factioncreepers.listeners.dynamite;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class DynamiteExplode implements Listener {

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity.getCustomName() == null || entity.getType() != EntityType.CREEPER) {
            return;
        }

        if (Enable.pl().color(Storage.DynamiteConfiguration.getString("Nametag.Format")).equals(entity.getCustomName())) {
            // Bukkit.broadcastMessage("dynamite explode");
            e.setCancelled(true);
            ConfigurationSection configuration = Storage.DynamiteConfiguration;
            Location location = entity.getLocation();
            entity.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), Float.parseFloat(configuration.get("Info.Explosion-Power") + ""),
                    configuration.getBoolean("Info.Ignite-Blocks")
                    , configuration.getBoolean("Info.Break-Blocks"));

            // Bukkit.broadcastMessage(Float.parseFloat(configuration.get("Info.Explosion-Power") + "") + "  " + configuration.getBoolean("Info.Ignite-Blocks") + "  " + configuration.getBoolean("Info.Break-Blocks"));
        }

    }


}
