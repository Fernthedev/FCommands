package com.github.fernthedev.fcommands.spigotclass.misc;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fernapi.universal.Universal;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class RideBow implements Listener {


    private static ShapelessRecipe shapelessRecipe;

    private static Map<UUID, Team> teamMap = new HashMap<>();

    private static Map<Arrow, Player> ignoreEntityHit = new HashMap<>();

    public RideBow(Plugin plugin) {

        // Our custom variable which we will be changing around.
        ItemStack item = new ItemStack(Material.BOW);

// The meta of the diamond sword where we can change the name, and properties of the item.
        ItemMeta meta = item.getItemMeta();

        // This sets the name of the item.
// Instead of the § symbol, you can use ChatColor.<color>
        meta.setDisplayName("§5Teleport Bow");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Riding");
        lore.add(ChatColor.DARK_GREEN + "Hold shift to just teleport rather than ride");

        meta.setLore(lore);

// Set the meta of the sword to the edited meta.
        item.setItemMeta(meta);

// Add the custom enchantment to make the emerald sword special
// In this case, we're adding the permission that modifies the damage value on level 5
// Level 5 is represented by the second parameter. You can change this to anything compatible with a sword
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 5);


// We will initialise the next variable after changing the properties of the sword

        NamespacedKey key = new NamespacedKey(plugin, "teleport_bow");
        shapelessRecipe = new ShapelessRecipe(key, item);

        shapelessRecipe.addIngredient(Material.BOW);
        shapelessRecipe.addIngredient(Material.ENDER_PEARL);

        Bukkit.addRecipe(shapelessRecipe);
    }

    private void recurseEject(Entity e) {
        e.getPassengers().parallelStream().forEach(this::recurseEject);

        e.eject();
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getRecipe() == shapelessRecipe && !p.hasPermission("fernc.craft.bow")) e.setCancelled(true);
    }

    //boolean teleport = false;
    //Player pl = null;

    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow
                && e.getEntity() instanceof Player // Check entity types
                && ignoreEntityHit.containsKey((Arrow) e.getDamager()) // Check if the ignore should be done
                && ignoreEntityHit.get((Arrow) e.getDamager()) == e.getEntity()) { // Check if both entities are the rider
             Arrow arrow = (Arrow) e.getDamager();
             Player player = (Player) e.getEntity();

             e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) e.getEntity();
            if (arrow.hasMetadata("teleport")) {

                List<MetadataValue> tp = arrow.getMetadata("teleport");
                if (!tp.isEmpty()) {
                    double telepx = arrow.getLocation().getX();
                    double telepy = arrow.getLocation().getY();
                    double telepz = arrow.getLocation().getZ();
                    Player player = (Player) arrow.getMetadata("teleport").get(0).value();
                    boolean riding = arrow.getMetadata("teleport_ride").get(0).asBoolean();
                    Universal.debug(player + " shot an arrow with metadata ");
                    World telepworld = arrow.getWorld();

                    List<Entity> entities = new ArrayList<>(arrow.getPassengers());

                    if ((player.getVehicle() != null && player.getVehicle() == arrow) || (!riding)) {
                        if (player.getVehicle() != null)
                            player.getVehicle().eject();


                        player.teleport(new Location(telepworld, telepx, telepy, telepz));

                        World world = player.getWorld();
                        world.spawnParticle(Particle.TOTEM, player.getLocation(), 40);
                    }

                    removeArrow(arrow);
//                    arrow.getPassengers().parallelStream().filter(Objects::nonNull).forEach(Entity::eject);

//                    entities.parallelStream().filter(entity -> e instanceof Player).forEach(Entity::remove);






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

    private void removeArrow(Arrow arrow) {
        arrow.eject();

        if (teamMap.containsKey(arrow.getUniqueId())) {
            Team team = teamMap.get(arrow.getUniqueId());

            try {
                team.unregister();
            } catch (IllegalStateException ignored) {}
        }

        teamMap.remove(arrow.getUniqueId());

        ignoreEntityHit.remove(arrow);


        arrow.remove();
    }


    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        //FernCommands.getInstance().getLogger().info(e.getEntity() + " SHOT AN ARROW");
        if (e.getEntity() instanceof Player && e.getProjectile() instanceof Arrow) {
            Player player = (Player) e.getEntity();
            if (player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                //FernCommands.getInstance().getLogger().info("is sneaking and shot bow");
                //List<String> lore = player.getItemOnCursor().getItemMeta().getLore();
                boolean hasLore = player.getInventory().getItemInMainHand().getItemMeta().hasLore();

                if (hasLore && player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.GRAY + "Riding")) {




                    //FernCommands.getInstance().getLogger().info("has lore and shot");
                    //e.getProjectile().addPassenger(player);'
                    //         //teleport = true;
                    //          //   pl = player;
                    //if (!e.getProjectile().getPassengers().contains(player)) {
                    //  player.sendMessage("Unable to ride projectile");
                    // }



                    Arrow arrow = (Arrow) e.getProjectile();

                    boolean creative = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;

                    if (!creative) {

                        if (!player.getInventory().contains(Material.ENDER_PEARL)) return;

                        ItemStack enderpearl = null;
                        int amount = 0;
                        ItemStack[] inv = player.getInventory().getContents();
                        for (ItemStack item : inv) {

                            if (item == null) continue;
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
                        } else {
                            player.sendMessage(ChatColor.RED + "Unable to get inventory content");
                            return;
                        }
                    }

                    arrow.setMetadata("teleport", new FixedMetadataValue(FernCommands.getInstance(), player));
                    arrow.setMetadata("teleport_ride", new FixedMetadataValue(FernCommands.getInstance(), !player.isSneaking()));


                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            //play effects
                            World world = player.getWorld();
                            world.spawnParticle(Particle.SPELL, player.getLocation(), 200);
                            World ar = arrow.getWorld();
                            ar.spawnParticle(Particle.END_ROD, arrow.getLocation(), 20);
                            if (arrow.isDead()) {
                                cancel();
                            }
                        }

                    }.runTaskTimer(FernCommands.getInstance(), 0L, 5L);

                    if (!player.isSneaking() ) {

                        Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(arrow.getUniqueId().toString().trim().replaceAll("-", "").substring(0, 15));
                        team.addEntry(arrow.getUniqueId().toString());

                        team.addEntry(player.getName());

                        List<Entity> passengers = player.getPassengers();

                        passengers.forEach(entity -> {
                            if (e instanceof Player) {
                                team.addEntry(((Player) e).getName());
                            } else {
                                team.addEntry(entity.getUniqueId().toString());
                            }
                        });

                        team.setAllowFriendlyFire(false);

                        teamMap.put(arrow.getUniqueId(), team);
                        ignoreEntityHit.put(arrow, player);


                        if (player.getVehicle() instanceof Arrow) {
                            Arrow oldArrow = (Arrow) player.getVehicle();
                            removeArrow(oldArrow);
                        }

                        arrow.addPassenger(player);

                        passengers.forEach(player::addPassenger);
                    }

                    // arrow.playEffect(EntityEffect.FIREWORK_EXPLODE);
                    //player.spawnParticle(Particle.SPELL,player.getLocation(),200);


                }
            }
        }
    }



//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
//        if (sender instanceof Player) {
//            Player player = (Player) sender;
//            if (player.hasPermission("fernc.craft.bow")) {
//                if (player.getInventory().getItemInMainHand().getType() == Material.BOW && player.getInventory().getItemInMainHand().getType() != null) {
//                    ItemStack bow = player.getInventory().getItemInMainHand();
//                    ItemMeta mainlore = bow.getItemMeta();
//                    boolean haslo = false;
//                    if (player.getInventory().getItemInMainHand().getItemMeta().hasLore())
//                        haslo = true;
//
//                    if (haslo) {
//                        if (player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.GRAY + "Riding")) {
//                            player.sendMessage(ChatColor.BLUE + "This bow already has riding enchant");
//                        }
//                    }
//                    if (!haslo) {
//                        if (player.getInventory().contains(Material.ENDER_PEARL)) {
//                            ArrayList<String> lore = new ArrayList<>();
//                            lore.add(ChatColor.GRAY + "Riding");
//                            mainlore.setLore(lore);
//                            ItemStack[] inv = player.getInventory().getContents();
//                            ItemStack enderpearl = null;
//                            int amount = 0;
//                            boolean creative = false;
//                            if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
//                                creative = true;
//                            }
//                            for (ItemStack item : inv) {
//                                //sender.sendMessage(item + " is in your inventory");
//                                //FernCommands.getInstance().getLogger().info(item + "is in " + player + "'s inventory");
//                                if (item.getType() == Material.ENDER_PEARL) {
//                                    amount = item.getAmount();
//                                    enderpearl = item;
//                                    //player.sendMessage(amount + "of enderpearls");
//                                    break;
//                                }
//                            }
//                            if (amount == 0) {
//                                if (!creative) {
//                                    sender.sendMessage(ChatColor.RED + "Failed to get inventory items or amount");
//                                    return true;
//                                }
//                            } else {
//                                if (!creative) {
//                                    amount -= 1;
//                                    enderpearl.setAmount(amount);
//                                }
//                                bow.setItemMeta(mainlore);
//                                sender.sendMessage(ChatColor.GREEN + "Enchanted bow with riding successfully");
//                                FernCommands.getInstance().getLogger().info("Successfully enchanted bow with riding");
//                                return true;
//                            }
//                        } else {
//                            player.sendMessage(ChatColor.RED + "YOU MUST BE HOLDING A BOW AND HAVE ATLEAST ONE ENDERPEARL");
//                            return true;
//                        }
//                    }
//                } else {
//                    player.sendMessage(ChatColor.RED + "YOU MUST BE HOLDING A BOW AND HAVE ATLEAST ONE ENDERPEARL");
//                    return true;
//                }
//            } else {
//                sender.sendMessage(ChatColor.RED + "You do not have permission to craft that item");
//                return true;
//            }
//        } else {
//            sender.sendMessage(ChatColor.RED + "You must be a player to craft stuff. k?");
//            return true;
//        }
//        return true;
//    }
}
