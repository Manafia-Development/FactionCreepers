package com.github.manafia.factioncreepers.listeners.rocket;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import com.github.manafia.factioncreepers.utilities.particles.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.util.Vector;

public class RocketCreeperSpawn implements Listener {


    @EventHandler
    public void onCreeperSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() != EntityType.CREEPER) {
            return;
        }

        // Using delayed task because without it the name shows up as null.
        Bukkit.getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {
            if (entity.getCustomName() == null) {
                return;
            }

            // entity.getPassenger().remove();

            // Bukkit.broadcastMessage("not null");
            if (!Enable.pl().color(Storage.RocketConfiguration.getString("Item.Name")).equals(entity.getCustomName())
                    && !Enable.pl().color(Storage.RocketConfiguration.getString("Nametag.Format")).equals(entity.getCustomName())) {
                return;
            }


            ConfigurationSection configuration = Storage.RocketConfiguration;
            Enable.pl().applyNametag(entity, configuration.getString("Nametag.Format"), configuration.getBoolean("Nametag.Visible"));
            int particleRunnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(Enable.pl(), () -> ParticleEffect.FLAME.display((float) 0.1, (float) 0.1, (float) 0.1, 0, 10, entity.getLocation(), 64)
                    , 5L, 5L);
            int velocityRunnable = Bukkit.getScheduler().scheduleSyncRepeatingTask(Enable.pl(), () -> {

                entity.setVelocity(new Vector(0, 1, 0));

            }, 5L, 5L);

            Bukkit.getScheduler().scheduleSyncDelayedTask(Enable.pl(), () -> {
                Bukkit.getScheduler().cancelTask(particleRunnable);
                Bukkit.getScheduler().cancelTask(velocityRunnable);
                if (entity.getPassenger() != null) {
                    entity.getPassenger().remove();
                }
                entity.remove();


                Location location = entity.getLocation();
                entity.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), Float.parseFloat(configuration.get("Info.Explosion-Power") + ""),
                        configuration.getBoolean("Info.Ignite-Blocks")
                        , configuration.getBoolean("Info.Break-Blocks"));
            }, 200L);
        }, 3L);


    }


}
