package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class RidePlayer implements Listener {
    @SuppressWarnings("deprecation")
@EventHandler
public void onPlayerClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Entity en = getNearestEntityInSight(p, 5);
        if (e.getAction() == Action.RIGHT_CLICK_AIR && en instanceof Player) {
            p.sendMessage("You have rightclicked a player.");
            Slime slime = (Slime) en.getWorld().spawnEntity(en.getLocation(), EntityType.SLIME);
            if (en.getPassengers() != null) {
                for (Entity epl : en.getPassengers()) {
                    Entity eppl = (Entity) epl.getPassengers();
                    for (Player epplayer = (Player) eppl.getPassengers(); ; ) {
                        p.setPassenger(slime);
                        epplayer.setPassenger(p);
                    }
                }
                p.setPassenger(slime);
                en.setPassenger(p);
            }
        }
    }


    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight(null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (int i = 0;i<sightBlock.size();i++)
            sight.add(sightBlock.get(i).getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null;
    }
}
