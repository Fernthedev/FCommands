package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class RidePlayer implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    public static void onPlayerClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked().getType() == EntityType.PLAYER) {
            Player en = (Player) e.getRightClicked();
            Player p = e.getPlayer();
            Slime slime = (Slime) en.getWorld().spawnEntity(en.getLocation(), EntityType.SLIME);
            /*if(en.getPassenger().getPassenger() == p) {
                e.setCancelled(true);
            }*/
            slime.remove();
            slime.setSilent(true);
            slime.setSize(2);
            slime.setInvulnerable(true);
            slime.setCollidable(false);
            slime.setAI(false);
            slime.setCustomName(p.getDisplayName() + " " + p.getUniqueId());
           //slime.setHealth(900000000);
            //slime.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 9999999, false, false));
            en.addPassenger(slime);
            slime.setPassenger(p);
           /*if (en.getPassenger() != null) {
//PassengerHandling
               p.setPassenger(slime);*/
//NMSPacketHandling
               //PacketPlayOutMount packet = new PacketPlayOutMount(((CraftPlayer)player).getHandle());
            }
        }

    public static void onEnable() {
        Bukkit.getLogger().info("LOADED RIDER CLASS");
    }
    @SuppressWarnings("deprecation")
    @EventHandler
    public static void onPlayerDismount(EntityDismountEvent e) {
       // p.sendMessage(e + " Was your vehicle");
        Entity sl = e.getDismounted();
        Entity p = e.getEntity();
        if(p.getVehicle() != p) {
            //sl.remove();
            p.getVehicle();
        /*if(!sl.isDead()) {
            p.sendMessage("slime still alive");
        }*/
          //  while (!sl.isDead()) {
           //     sl.remove();
          //      //p.sendMessage("tried to kill entity");
          //  }
        }


    }
}



