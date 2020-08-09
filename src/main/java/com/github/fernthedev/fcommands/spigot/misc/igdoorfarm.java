package com.github.fernthedev.fcommands.spigot.misc;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.logging.Logger;

public class igdoorfarm implements Listener {
    private Logger log = FernCommands.getInstance().getServer().getLogger();

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL && e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) {
            if(e.getEntity().getType() == EntityType.IRON_GOLEM) {
                e.getEntity().remove();
            }

        }
        //if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            /*e.getEntity().setAI(false);
            if(e.getEntity().hasAI()) {
                log.warning("Unable to disable ai");
                e.getEntity().setAI(true);
            }*/
            //LivingEntity entity = (LivingEntity) e.getEntity();
            //overrideBehavior(e.getEntity());
            //EntityCreature c = (EntityCreature) ((EntityInsentient)((CraftEntity)entity).getHandle());
       // }
    }

    @EventHandler
    public void onCreatureDie(EntityDamageEvent e) {
        if(e.getEntity().getType() == EntityType.BLAZE && e.getCause() == EntityDamageEvent.DamageCause.DROWNING ) {
            e.setCancelled(true);
        }
    }
}
