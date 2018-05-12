package io.github.fernthedev.fcommands.spigotclass;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ridebow implements Listener,CommandExecutor {


    //boolean teleport = false;
    //Player pl = null;

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
     if(e.getEntity() instanceof Arrow) {
         Arrow arrow = (Arrow) e.getEntity();
         if(arrow.hasMetadata("teleport")) {
             List<MetadataValue> tp = arrow.getMetadata("teleport");
             if (!tp.isEmpty()) {
                 double telepx = arrow.getLocation().getX();
                 double telepy = arrow.getLocation().getY();
                 double telepz = arrow.getLocation().getZ();
                 Player player = (Player) arrow.getMetadata("teleport").get(0).value();
                 FernCommands.getInstance().getLogger().info(player + " shot an arrow with metadata ");
                 World telepworld = arrow.getWorld();
                 player.teleport(new Location(telepworld,telepx,telepy,telepz));
                 arrow.remove();
                 World world = player.getWorld();
                 world.spawnParticle(Particle.TOTEM,player.getLocation(),10);
             }
         }


    //     if(teleport) {
            // Location telep = arrow.getLocation();
          //   if(pl != null)
          //   pl.teleport(telep);
         //    teleport = false;
      //       pl = null;
   //      }
         }
     }


    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        //FernCommands.getInstance().getLogger().info(e.getEntity() + " SHOT AN ARROW");
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getInventory().getItemInMainHand().getType() == Material.BOW && player.isSneaking()) {
                //FernCommands.getInstance().getLogger().info("is sneaking and shot bow");
                //List<String> lore = player.getItemOnCursor().getItemMeta().getLore();
                boolean haslo = false;
                if(player.getInventory().getItemInMainHand().getItemMeta().hasLore())
                    haslo = true;
                if(haslo) {
                    if (player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.GRAY + "Riding") && player.getInventory().contains(Material.ENDER_PEARL)) {
                        //FernCommands.getInstance().getLogger().info("has lore and shot");
                        //e.getProjectile().addPassenger(player);'
               //         //teleport = true;
           //          //   pl = player;
                        //if (!e.getProjectile().getPassengers().contains(player)) {
                          //  player.sendMessage("Unable to ride projectile");
                       // }
                        Arrow arrow = null;
                        if(e.getProjectile() instanceof Arrow) {
                            arrow = (Arrow) e.getProjectile();
                        }
                        if(arrow != null)
                            arrow.setMetadata("teleport", new FixedMetadataValue(FernCommands.getInstance(), player));

                        boolean creative = false;
                        if (player.getGameMode() != GameMode.CREATIVE || player.getGameMode() != GameMode.SPECTATOR)
                            creative = true;
                        if (!creative) {
                            ItemStack enderpearl = null;
                            int amount = 0;
                            ItemStack[] inv = player.getInventory().getContents();
                            for (ItemStack item : inv) {
                                //sender.sendMessage(item + " is in your inventory");
                                //FernCommands.getInstance().getLogger().info(item + "is in " + player + "'s inventory");
                                if (item.getType() == Material.ENDER_PEARL) {
                                    amount = item.getAmount();
                                    enderpearl = item;
                                    //player.sendMessage(amount + "of enderpearls");
                                    break;
                                }
                            }
                            if (amount != 0) {
                                amount -= 1;
                                enderpearl.setAmount(amount);
                            }else{
                                player.sendMessage(ChatColor.RED + "Unable to get inventory content");
                            }
                        }
                        Arrow finalArrow = arrow;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                //play effects
                                if(finalArrow != null) {
                                    World world = player.getWorld();
                                    world.spawnParticle(Particle.SPELL, player.getLocation(), 200);
                                    World ar = finalArrow.getWorld();
                                    ar.spawnParticle(Particle.END_ROD, finalArrow.getLocation(),20);
                                }
                                if (finalArrow == null || finalArrow.isDead()) {
                                    cancel();
                                }
                            }

                        }.runTaskTimer(FernCommands.getInstance(), 0L, 5L);
                        }
                               // arrow.playEffect(EntityEffect.FIREWORK_EXPLODE);
                        //player.spawnParticle(Particle.SPELL,player.getLocation(),200);


                    }
                }
            }
        }



    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("fernc.craft.bow")) {
                    if (player.getInventory().getItemInMainHand().getType() == Material.BOW && player.getInventory().getItemInMainHand().getType() != null) {
                        ItemStack bow = player.getInventory().getItemInMainHand();
                        ItemMeta mainlore = bow.getItemMeta();
                        boolean haslo = false;
                        if (player.getInventory().getItemInMainHand().getItemMeta().hasLore())
                            haslo = true;

                        if (haslo) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.GRAY + "Riding")) {
                                player.sendMessage(ChatColor.BLUE + "This bow already has riding enchant");
                            }
                        }
                        if (!haslo) {
                            if (player.getInventory().contains(Material.ENDER_PEARL)) {
                                ArrayList<String> lore = new ArrayList<>();
                                lore.add(ChatColor.GRAY + "Riding");
                                mainlore.setLore(lore);
                                ItemStack[] inv = player.getInventory().getContents();
                                ItemStack enderpearl = null;
                                int amount = 0;
                                boolean creative = false;
                                if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
                                    creative = true;
                                }
                                for (ItemStack item : inv) {
                                    //sender.sendMessage(item + " is in your inventory");
                                    //FernCommands.getInstance().getLogger().info(item + "is in " + player + "'s inventory");
                                    if (item.getType() == Material.ENDER_PEARL) {
                                        amount = item.getAmount();
                                        enderpearl = item;
                                        //player.sendMessage(amount + "of enderpearls");
                                        break;
                                    }
                                }
                                if (amount == 0) {
                                    if (!creative) {
                                        sender.sendMessage(ChatColor.RED + "Failed to get inventory items or amount");
                                        return true;
                                    }
                                } else {
                                    if (!creative) {
                                        amount -= 1;
                                        enderpearl.setAmount(amount);
                                    }
                                    bow.setItemMeta(mainlore);
                                    sender.sendMessage(ChatColor.GREEN + "Enchanted bow with riding successfully");
                                    FernCommands.getInstance().getLogger().info("Successfully enchanted bow with riding");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.RED + "YOU MUST BE HOLDING A BOW AND HAVE ATLEAST ONE ENDERPEARL");
                                return true;
                            }
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "YOU MUST BE HOLDING A BOW AND HAVE ATLEAST ONE ENDERPEARL");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You do not have permission to craft that item");
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You must be a player to craft stuff. k?");
                return true;
            }
        return true;
    }
}
