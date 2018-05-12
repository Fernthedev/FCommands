package io.github.fernthedev.fcommands.spigotclass;

import com.google.common.collect.Sets;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathfinderGoalSelector;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.lang.reflect.Field;
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

    public  void overrideBehavior(LivingEntity e) {
        EntityCreature c = (EntityCreature) e;
        //This gets the EntityCreature, we need it to change the values

        try {
            Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(c.goalSelector, Sets.newLinkedHashSet());
            bField.set(c.targetSelector, Sets.newLinkedHashSet());
            cField.set(c.goalSelector, Sets.newLinkedHashSet());
            cField.set(c.targetSelector, Sets.newLinkedHashSet());
            //this code clears fields B, C. so right now the mob wont walk

        } catch (Exception exc) {exc.printStackTrace();}
    }
}
