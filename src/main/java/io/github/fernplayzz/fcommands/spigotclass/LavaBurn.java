package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LavaBurn implements Listener {
    @EventHandler
    public static void onPlayerPickupItem(PlayerPickupItemEvent event) {
    if(event.getItem().getItemStack().equals(new ItemStack(Material.LAVA_BUCKET))) {
        event.getPlayer().sendMessage("YOU PICKED UP " + event.getItem().getItemStack());

        Random fire = new Random();
        int  firechance = fire.nextInt(100) + 1;
        if (firechance<=80) {
            Player target = event.getPlayer();
            Random firetick = new Random();
            int numbertick = firetick.nextInt(100) + 1;
            if (numbertick >= 40) {
                target.setFireTicks(numbertick * 2);
            }
        }
    }
    }

    public static void onEnable() {
        Bukkit.getLogger().info("ENABLED LAVA BUCKET");
    }

}
