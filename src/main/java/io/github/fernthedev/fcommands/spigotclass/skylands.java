package io.github.fernthedev.fcommands.spigotclass;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import org.bukkit.Bukkit;
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

import java.util.Random;


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
                    int x = (int) player.getLocation().getX();
                    int z = (int) player.getLocation().getZ();
                    //int y = Bukkit.getWorld("skyland").getHighestBlockYAt(player.getLocation());
                    int y = 256;
                    Location goworld = new Location(Bukkit.getWorld("skyland"),x,y,z,player.getLocation().getYaw(),player.getLocation().getPitch());


                    String type = plocation.getBlock().getType().toString();

                    player.sendMessage(FernCommands.message("Going to find a place to send you"));

                    if(goworld.getBlock().getType() == Material.AIR || goworld.getBlock().getType() != Material.AIR) {
                        boolean keepcheck = true;

                        int newy = y;
                        while(keepcheck) {
                            newy = newy-1;
                            if(Bukkit.getWorld("skyland").getBlockAt(x,newy,z).getType() != Material.AIR && Bukkit.getWorld("skyland").getBlockAt(x,newy+1,z).getType() == Material.AIR && newy != 0 ) {
                                y = newy;
                                keepcheck = false;
                            }
                            if(newy == 0) {
                                Bukkit.getWorld("skyland").getBlockAt(x,250,z).setType(Material.GRASS);
                                keepcheck = false;
                            }
                        }
                    }
                    goworld = new Location(Bukkit.getWorld("skyland"), x, y + 1, z,plocation.getYaw(), plocation.getPitch());

                    player.sendMessage("Found a place checking if you can build. It's at " + goworld.getX() + " " + goworld.getY() + " " + goworld.getZ());


                    if(FernCommands.getInstance().isIsWorldGuard()) {
                        if(!FernCommands.getInstance().getWorldGuardPlugin().canBuild(player,goworld)) {
                            player.sendMessage("You can't build here? why. Lets find another place");
                        }
                        while (!FernCommands.getInstance().getWorldGuardPlugin().canBuild(player, goworld)) {
                            Random random = new Random();
                            goworld.setX(random.nextDouble());
                            goworld.setZ(random.nextDouble());

                            if (goworld.getBlock().getType() == Material.AIR || goworld.getBlock().getType() != Material.AIR) {
                                boolean keepcheck = true;

                                int newy = y;
                                while (keepcheck) {
                                    newy = newy - 1;
                                    if (Bukkit.getWorld("skyland").getBlockAt(x, newy, z).getType() != Material.AIR && Bukkit.getWorld("skyland").getBlockAt(x, newy + 1, z).getType() == Material.AIR && newy != 0) {
                                        y = newy;

                                        keepcheck = false;
                                    }

                                    if (newy == 0) {
                                        Bukkit.getWorld("skyland").getBlockAt(x, 255, z).setType(Material.GRASS);

                                        keepcheck = false;
                                    }
                                }
                                goworld = new Location(Bukkit.getWorld("skyland"), x, y + 1, z, plocation.getYaw(), plocation.getPitch());
                                player.sendMessage("Found a new place checking if you can build. It's at " + goworld.getX() + " " + goworld.getY() + " " + goworld.getZ());
                            }
                        }
                    }


                    //if((goworld.getBlock().getType() == Material.AIR)) {
                        //new FernCommands().infolog(y + " is the highest level for player " + player);
                   //     y = 256;
                        //MultiverseCore.addPlayerToTeleportQueue(String.valueOf(world), player.getDisplayName());
                        //player.teleport(goworld)
                        //MultiverseWorld world = wm.get
                //    }
                        /*
                    if(goworld.getBlock().getType() == Material.AIR && y == 256) {
                        player.sendMessage(ChatColor.RED + "You are about to fall at y level 256 or void in 5 seconds.");
                        try {
                            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    player.teleport(goworld);
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
                player.teleport(new Location(Bukkit.getServer().getWorlds().get(0),player.getLocation().getX(),256,player.getLocation().getZ()));
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
