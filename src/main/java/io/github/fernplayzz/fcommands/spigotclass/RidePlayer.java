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
        Player en = (Player) e.getRightClicked();
        Player p = e.getPlayer();
        Slime slime = (Slime) en.getWorld().spawnEntity(en.getLocation(), EntityType.SLIME);
        slime.setSilent(true);
        slime.setSize(2);
        slime.setInvulnerable(true);
        slime.setCollidable(false);
        slime.setAI(false);
        slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 9999999, false, false));
            if (e.getRightClicked().getType() == EntityType.PLAYER) {
                en.addPassenger(slime);
                slime.addPassenger(p);
                if (en.getPassengers() != null) {
                    for (Entity epl : en.getPassengers()) {
                        Entity eppl = (Entity) epl.getPassengers();
                        for (Player epplayer = (Player) eppl.getPassengers(); ; ) {
                            epplayer.addPassenger(slime);
                            slime.addPassenger(p);
                            //epplayer.setPassenger(p);
                        }
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
        Entity sl = e.getDismounted();
        Player en = (Player) sl.getPassenger();
        Player p = (Player) e.getEntity();
        Slime slime = (Slime) en.getWorld().spawnEntity(en.getLocation(), EntityType.SLIME);
        slime.setSilent(true);
        slime.setSize(2);
        slime.setInvulnerable(true);
        slime.setCollidable(false);
        slime.setAI(false);
        slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 9999999, false, false));
        slime.remove();
    }
}



