package com.github.manafia.factioncreepers.listeners;


import com.github.manafia.factioncreepers.Enable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;


public class CreeperDeath implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Entity entity = e.getEntity();

        if (entity.getType() != EntityType.CREEPER || entity.getCustomName() == null) {
            return;
        }
        if (Enable.mc18) {

            removePassenger(entity);
        }

    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        Entity entity = e.getEntity();
        if (entity.getType() != EntityType.CREEPER || entity.getCustomName() == null) {
            return;
        }


        if (Enable.mc18) {

            removePassenger(entity);
        }

    }


    private void removePassenger(Entity creeper) {
        if (creeper.getPassenger() == null || creeper.getPassenger().getType() != EntityType.ARMOR_STAND) {
            return;
        }
        creeper.getPassenger().remove();
    }


}
