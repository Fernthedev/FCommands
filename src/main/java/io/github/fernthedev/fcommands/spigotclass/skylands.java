package io.github.fernthedev.fcommands.spigotclass;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;


/**
 * This is used to make the skylands if you have SB-Skylands and Multiverse, although it requries a world called "skyland"
 */

public class skylands implements Listener {
    // set this one outside any function, but inside the class
    //boolean thrown = false;
   // MultiverseCore getMultiverseCore;

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        /*
      To print out using the plugin name prefix
     */

        //thrown = true;
        //if (!(event.getEntity().getShooter() instanceof Player)) { return; }
        //getMultiverseCore = new FernCommands().getMultiverseCore();
        if (event.getEntity().toString().contentEquals("CraftEnderPearl") || event.getEntity().getType() == EntityType.ENDER_PEARL) {
            //thrown = true;
            Player player = (Player) event.getEntity().getShooter();
            Location hitlocation = event.getEntity().getLocation();
            Entity enderpearl = event.getEntity();
            if (player.getEyeLocation().getY() >= 240 && player.getLocation().getPitch() == -90 && player.getWorld() != Bukkit.getWorld("skyland")) {
                WorldManager wm = new WorldManager(getMultiverseCore());
                //new FernCommands().infolog("Enderpearl launched at 240 or higher and looking straight up");
                //WorldManager wm = new WorldManager(getMultiverseCore());
                //WorldManager wm = new WorldManager(getMultiverseCore());
                MultiverseWorld world = wm.getMVWorld("skyland");
                //Bukkit.getServer().getLogger().info("Multiverse worlds found: " + worlds);
                if (Bukkit.getWorld("skyland").getLoadedChunks() != null) {
                    //for (Object worlde : worlds) {
                    //MultiverseWorld world = wm.getMVWorld((String) worlde);
                    //MultiverseWorld world = wm.get
                    enderpearl.remove();
                    Location plocation = player.getLocation();
                    Location goworld = new Location(Bukkit.getWorld(String.valueOf(world)), hitlocation.getX(), hitlocation.getY(), hitlocation.getZ());
                    int x =plocation.getBlockX();
                    int z = plocation.getBlockZ();
                    //int y = Bukkit.getWorld("skyland").getHighestBlockYAt(player.getLocation());
                    int y = 256;
                    String type = plocation.getBlock().getType().toString();
                    if(Bukkit.getWorld("skyland").getBlockAt(x,y,z).getType() == Material.AIR) {
                        for(; ;) {
                            y = y-1;
                            if(!(Bukkit.getWorld("skyland").getBlockAt(x,y,z).getType() == Material.AIR)) {
                               break;
                            }
                            if(y == 0) {
                                y = 256;
                                break;
                            }
                        }
                    }


                    //if((Bukkit.getWorld("skyland").getBlockAt(x,y,z).getType() == Material.AIR)) {
                        //new FernCommands().infolog(y + " is the highest level for player " + player);
                   //     y = 256;
                        //MultiverseCore.addPlayerToTeleportQueue(String.valueOf(world), player.getDisplayName());
                        //player.teleport(goworld)
                        //MultiverseWorld world = wm.get
                //    }
                    if(Bukkit.getWorld("skyland").getBlockAt(x,y,z).getType() == Material.AIR && y == 256) {
                        player.sendMessage(ChatColor.RED + "You are about to fall at y level 256 or void in 5 seconds.");
                        try {
                            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    player.teleport(new Location(Bukkit.getWorld("skyland"), x, y + 1, z,plocation.getYaw(), plocation.getPitch()));
                }
                //player.teleport(hitlocation);
            }
        }
    }

    @EventHandler
    public void onFallVoid(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            //If the entity is a player
            Player player = (Player) e.getEntity();
            //Create a new variable for the player
            if ((e.getCause() == EntityDamageEvent.DamageCause.VOID) && player.getWorld() == Bukkit.getWorld("skyland")) {
                //If the cause of the event is the void
                e.setCancelled(true);
                int x = Bukkit.getWorld("skyland").getHighestBlockAt(player.getLocation()).getX();
                int z = Bukkit.getWorld("skyland").getHighestBlockAt(player.getLocation()).getY();
                player.teleport(new Location(Bukkit.getServer().getWorlds().get(0), x, 256, z));
                //Teleport the player to the world's spawn-location
                //player.sendMessage(ChatColor.DARK_AQUA + "You've been teleported back to spawn!");
                //Send the player a fancy message saying he was being stupid
            }
        }
    }

    public MultiverseCore getMultiverseCore() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }

        throw new RuntimeException("MultiVerse not found!");
    }


}
