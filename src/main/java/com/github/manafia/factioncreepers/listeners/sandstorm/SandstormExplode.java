package com.github.manafia.factioncreepers.listeners.sandstorm;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.concurrent.ThreadLocalRandom;

public class SandstormExplode implements Listener {

    @EventHandler
    public void onCreeperExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        if (entity.getCustomName() == null
                || entity.getType() != EntityType.CREEPER
                || !Enable.pl().color(Storage.SandstormConfiguration.getString("Nametag.Format")).equals(entity.getCustomName())) {
            return;
        }


        //  Bukkit.broadcastMessage("sandstorm explode");
        e.setCancelled(true);
        ConfigurationSection configuration = Storage.SandstormConfiguration;
        Location location = entity.getLocation();
        for (int i = 0; i < configuration.getInt("Info.Sand-Amount"); i++) {
            entity.getWorld().spawnFallingBlock(entity.getLocation().add(ThreadLocalRandom.current().nextInt(-4, 4), 3, ThreadLocalRandom.current().nextInt(-4, 4)), org.bukkit.Material.SAND, (byte) 0);
        }
        entity.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), Float.parseFloat(configuration.get("Info.Explosion-Power") + ""),
                configuration.getBoolean("Info.Ignite-Blocks")
                , configuration.getBoolean("Info.Break-Blocks"));

        //Bukkit.broadcastMessage(Float.parseFloat(configuration.get("Info.Explosion-Power") + "") + "  " + configuration.getBoolean("Info.Ignite-Blocks") + "  " + configuration.getBoolean("Info.Break-Blocks"));
    }

}
