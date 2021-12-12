package com.github.fernthedev.fcommands.spigot.misc;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class IronGolemDoorFarm implements Listener {

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) {
            if(e.getEntity().getType() == EntityType.IRON_GOLEM) {
                e.getEntity().remove();
            }

        }
    }

    @EventHandler
    public void onCreatureDie(EntityDamageEvent e) {
        if(e.getEntity().getType() == EntityType.BLAZE && e.getCause() == EntityDamageEvent.DamageCause.DROWNING ) {
            e.setCancelled(true);
        }
    }
}
