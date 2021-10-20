package com.github.manafia.factioncreepers.listeners.sticky;

import com.github.manafia.factioncreepers.Enable;
import com.github.manafia.factioncreepers.Storage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class StickyCreeperSuffocate implements Listener {

    @EventHandler
    public void onStickySuffocate(EntityDamageEvent e) {


        if (e.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION
                || e.getEntityType() != EntityType.CREEPER) {
            return;
        }
        Entity entity = e.getEntity();


        if (entity.getCustomName() == null
                || !Enable.pl().color(Storage.StickyConfiguration.getString("Nametag.Format")).equals(entity.getCustomName())) {
            return;
        }


        e.setCancelled(true);


    }


}
