package com.github.fernthedev.fcommands.spigot.misc;

import org.bukkit.event.Listener;


/**
 * This is used to make the skylands if you have SB-Skylands and Multiverse, although it requries a world called "skyland"
 */
public class Skylands implements Listener {
    // set this one outside any function, but inside the class
    //boolean thrown = false;
   // MultiverseCore getMultiverseCore;
//
//    @EventHandler
//    public void onProjectileLaunch(ProjectileLaunchEvent event) {
//        /*
//      To print out using the plugin name prefix
//     */
//
//
//        if (event.getEntity().toString().contentEquals("CraftEnderPearl") || event.getEntity().getType() == EntityType.ENDER_PEARL) {
//            //thrown = true;
//            Player player = (Player) event.getEntity().getShooter();
//            Entity enderpearl = event.getEntity();
//
//            if (player.getEyeLocation().getY() >= 240 && player.getLocation().getPitch() == -90 && player.getWorld() != Bukkit.getWorld("skyland")) {
//
//                if (Bukkit.getWorld("skyland").getLoadedChunks() != null) {
//                    //for (Object worlde : worlds) {
//                    //MultiverseWorld world = wm.getMVWorld((String) worlde);
//                    //MultiverseWorld world = wm.get
//                    enderpearl.remove();
//                    Location plocation = player.getLocation();
//                    int x = (int) player.getLocation().getX();
//                    int z = (int) player.getLocation().getZ();
//                    //int y = BUKKIT.getWorld("skyland").getHighestBlockYAt(player.getLocation());
//                    int y = 256;
//                    Location goworld = new Location(Bukkit.getWorld("skyland"),x,y,z,player.getLocation().getYaw(),player.getLocation().getPitch());
//
//
//
//                    player.sendMessage(FernCommands.message("Going to find a place to send you"));
//
//                    if(goworld.getBlock().getType() == Material.AIR || goworld.getBlock().getType() != Material.AIR) {
//                        boolean keepcheck = true;
//
//                        int newy = y;
//                        while(keepcheck) {
//                            newy = newy-1;
//                            if(Bukkit.getWorld("skyland").getBlockAt(x,newy,z).getType() != Material.AIR && Bukkit.getWorld("skyland").getBlockAt(x,newy+1,z).getType() == Material.AIR && newy != 0 ) {
//                                y = newy;
//                                keepcheck = false;
//                            }
//                            if(newy == 0) {
//                                Bukkit.getWorld("skyland").getBlockAt(x,250,z).setType(Material.GRASS);
//                                keepcheck = false;
//                            }
//                        }
//                    }
//                    goworld = new Location(Bukkit.getWorld("skyland"), x, y + 1, z,plocation.getYaw(), plocation.getPitch());
//
//                    player.sendMessage("Found a place checking if you can build. It's at " + goworld.getX() + " " + goworld.getY() + " " + goworld.getZ());
//
//
//                    if(HookManager.isWorldGuard()) {
////                        if(!FernCommands.getHookManager().getWorldGuardPlugin().(player,goworld)) {
////                            player.sendMessage("You can't build here? why. Lets find another place");
////                        }
////                        while (!FernCommands.getHookManager()
////                                .getWorldGuardPlugin().canBuild(player, goworld)) {
//                            Random random = new Random();
//                            goworld.setX(random.nextDouble());
//                            goworld.setZ(random.nextDouble());
//
//                            if (goworld.getBlock().getType() == Material.AIR || goworld.getBlock().getType() != Material.AIR) {
//                                boolean keepcheck = true;
//
//                                int newy = y;
//                                while (keepcheck) {
//                                    newy = newy - 1;
//                                    if (Bukkit.getWorld("skyland").getBlockAt(x, newy, z).getType() != Material.AIR && Bukkit.getWorld("skyland").getBlockAt(x, newy + 1, z).getType() == Material.AIR && newy != 0) {
//                                        y = newy;
//
//                                        keepcheck = false;
//                                    }
//
//                                    if (newy == 0) {
//                                        Bukkit.getWorld("skyland").getBlockAt(x, 255, z).setType(Material.GRASS);
//
//                                        keepcheck = false;
//                                    }
//                                }
//                                goworld = new Location(Bukkit.getWorld("skyland"), x, y + 1, z, plocation.getYaw(), plocation.getPitch());
//                                player.sendMessage("Found a new place checking if you can build. It's at " + goworld.getX() + " " + goworld.getY() + " " + goworld.getZ());
//                            }
//                        }
////                    }
//
//
//                    //if((goworld.getBlock().getType() == Material.AIR)) {
//                        //new FernCommands().infolog(y + " is the highest level for player " + player);
//                   //     y = 256;
//                        //MultiverseCore.addPlayerToTeleportQueue(String.valueOf(world), player.getDisplayName());
//                        //player.teleport(goworld)
//                        //MultiverseWorld world = wm.get
//                //    }
//                        /*
//                    if(goworld.getBlock().getType() == Material.AIR && y == 256) {
//                        player.sendMessage(ChatColor.RED + "You are about to fall at y level 256 or void in 5 seconds.");
//                        try {
//                            Thread.sleep(TimeUnit.SECONDS.toMillis(5));
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }*/
//                    player.teleport(goworld);
//                }
//                //player.teleport(hitlocation);
//            }
//        }
//    }
//
//    @EventHandler
//    public void onFallVoid(EntityDamageEvent e) {
//        if (e.getEntity() instanceof Player player) {
//            //If the entity is a player
//            //Create a new variable for the player
//            if ((e.getCause() == EntityDamageEvent.DamageCause.VOID) && player.getWorld() == Bukkit.getWorld("skyland")) {
//                //If the cause of the event is the void
//                e.setCancelled(true);
//                player.teleport(new Location(Bukkit.getServer().getWorlds().get(0),player.getLocation().getX(),256,player.getLocation().getZ()));
//
//            }
//        }
//    }


}
