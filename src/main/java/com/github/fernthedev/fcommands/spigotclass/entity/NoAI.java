package com.github.fernthedev.fcommands.spigotclass.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@Deprecated
public class NoAI implements Listener {

    @EventHandler
    public void onEntitySpawn(CreatureSpawnEvent e) {
        if(e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM || e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            setNoAI(e.getEntity());
        }
    }

    public void setNoAI(Entity e) {

//        net.minecraft.server.v1_12_R1.Entity nmsEntity = ((CraftEntity) e).getHandle();
//
//        NBTTagCompound tag = new NBTTagCompound();
//
//        nmsEntity.c(tag);
//
//        tag.setBoolean("NoAI", true);
//
//        EntityLiving el = (EntityLiving) nmsEntity;
//        el.a(tag);
    }

}
