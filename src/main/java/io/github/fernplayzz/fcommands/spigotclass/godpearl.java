package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 *
 *  This class is used to negate fall damage or any damage from using an enderpearl.
 *
 */
public class godpearl implements Listener {
    // set this one outside any function, but inside the class
    boolean doNotDamageThePlayerOnEnderPearlUse = false;



    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event)
    {
        doNotDamageThePlayerOnEnderPearlUse = true;
    }

    @EventHandler
    public void onProjectileHitEvent(ProjectileHitEvent event)
    {
        if (!(event.getEntity().getShooter() instanceof Player)) { return; }

        if (event.getEntity().toString().contentEquals("CraftEnderPearl"))
        {
            doNotDamageThePlayerOnEnderPearlUse = true;
            Player player = (Player) event.getEntity().getShooter();
            Location hitlocation = event.getEntity().getLocation();
            player.teleport(hitlocation);
        }
    }

    @EventHandler
    public void onEntityDamageEvent (EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player)) { return; }

        if (doNotDamageThePlayerOnEnderPearlUse)
        {
            event.setCancelled(true);
            doNotDamageThePlayerOnEnderPearlUse = false;
        }
    }
}
