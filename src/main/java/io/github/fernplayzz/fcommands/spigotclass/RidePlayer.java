package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class RidePlayer implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public static void onPlayerClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.PLAYER) {
            Player en = (Player) e.getRightClicked();
            Player p = e.getPlayer();
            Slime slime = (Slime) en.getWorld().spawnEntity(en.getLocation(), EntityType.SLIME);
            if(slime.getVehicle() == en) {
                e.setCancelled(true);
            }
            slime.setSilent(true);
            slime.setSize(2);
            slime.setInvulnerable(true);
            slime.getNoDamageTicks();
            slime.setCollidable(false);
            slime.setAI(false);
            slime.setHealth(900000000);
            slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 9999999, false, false));
            en.addPassenger(slime);
            slime.addPassenger(p);
            if (en.getPassengers() != null) {
                for (Entity epl : slime.getPassengers()) {
                    Entity eppl = (Entity) epl.getPassengers();
                    eppl.addPassenger(p);
                       // e.addPassenger(slime);
                       // slime.addPassenger(p);
                        //epplayer.setPassenger(p);
                }
                p.setPassenger(slime);
                en.setPassenger(p);
            }
        }
    }

    public static void onEnable() {
        Bukkit.getLogger().info("LOADED RIDER CLASS");
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public static void onPlayerDismount(EntityDismountEvent e) {
        Player p = (Player) e.getEntity();
        p.sendMessage(e.getDismounted() + " Was your vehicle");
        Entity sl = e.getDismounted();
        sl.eject();
        sl.remove();
        if(!sl.isDead()) {
            p.sendMessage("slime still alive");
        }
        if(sl.getVehicle().getType() == EntityType.PLAYER) {
            p.sendMessage("slime didnt dismount");
        }
    }
}



