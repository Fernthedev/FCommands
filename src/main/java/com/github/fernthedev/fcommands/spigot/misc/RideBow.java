package com.github.fernthedev.fcommands.spigot.misc;

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

import javax.inject.Inject;
import java.util.*;

public class RideBow implements Listener {



    private final Plugin plugin;

    private static ShapelessRecipe shapelessRecipe;

    private static final Map<UUID, Team> teamMap = new HashMap<>();

    private static final Map<Arrow, Player> ignoreEntityHit = new HashMap<>();

    @Inject
    public RideBow(Plugin plugin) {

        // Our custom variable which we will be changing around.
        ItemStack item = new ItemStack(Material.BOW);

// The meta of the diamond sword where we can change the name, and properties of the item.
        ItemMeta meta = item.getItemMeta();

        // This sets the name of the item.
// Instead of the § symbol, you can use ChatColor.<color>
        assert meta != null;
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
        if (shapelessRecipe == null) {
            NamespacedKey key = new NamespacedKey(plugin, "teleport_bow");

            shapelessRecipe = new ShapelessRecipe(key, item);

            shapelessRecipe.addIngredient(Material.BOW);
            shapelessRecipe.addIngredient(Material.ENDER_PEARL);

            Bukkit.addRecipe(shapelessRecipe);
        }
        this.plugin = plugin;
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



    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Arrow arrow
                && e.getEntity() instanceof Player player // Check entity types
                && ignoreEntityHit.containsKey((Arrow) e.getDamager()) // Check if the ignore should be done
                && ignoreEntityHit.get((Arrow) e.getDamager()) == e.getEntity()) { // Check if both entities are the rider

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLand(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Arrow arrow && arrow.hasMetadata("teleport")) {

            List<MetadataValue> tp = arrow.getMetadata("teleport");
            if (tp.isEmpty()) {
                return;
            }

            Player player = (Player) arrow.getMetadata("teleport").get(0).value();
            boolean riding = arrow.getMetadata("teleport_ride").get(0).asBoolean();
            Universal.debug(() -> player + " shot an arrow with metadata ");

            if (player != null && ((player.getVehicle() != null && player.getVehicle() == arrow) || (!riding))) {
                if (player.getVehicle() != null)
                    player.getVehicle().eject();

                Location arrowLocation = arrow.getLocation();
                double targetX = arrowLocation.getX();
                double targetY = arrowLocation.getY();
                double targetZ = arrowLocation.getZ();

                World targetWorld = arrow.getWorld();

                var playerLocation = player.getLocation();

                float yaw = playerLocation.getYaw();
                float pitch = playerLocation.getPitch();


                player.teleport(new Location(targetWorld, targetX, targetY, targetZ, yaw, pitch));

                World world = player.getWorld();
                world.spawnParticle(Particle.TOTEM, player.getLocation(), 40);
            }

            removeArrow(arrow);
        }
    }

    private void removeArrow(Arrow arrow) {
        arrow.eject();

        if (teamMap.containsKey(arrow.getUniqueId())) {
            Team team = teamMap.get(arrow.getUniqueId());

            try {
                team.unregister();
            } catch (IllegalStateException ignored) {
            }
        }

        teamMap.remove(arrow.getUniqueId());

        ignoreEntityHit.remove(arrow);


        arrow.remove();
    }


    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player player && e.getProjectile() instanceof Arrow arrow) {
            if (player.getInventory().getItemInMainHand().getType() == Material.BOW) {
                boolean hasLore = player.getInventory().getItemInMainHand().getItemMeta().hasLore();

                if (hasLore && player.getInventory().getItemInMainHand().getItemMeta().getLore().contains(ChatColor.GRAY + "Riding")) {

                    boolean creative = player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR;

                    if (!creative) {

                        if (!player.getInventory().contains(Material.ENDER_PEARL)) return;

                        ItemStack enderpearl = null;
                        int amount = 0;
                        ItemStack[] inv = player.getInventory().getContents();
                        for (ItemStack item : inv) {

                            if (item == null) continue;
                            if (item.getType() == Material.ENDER_PEARL) {
                                amount = item.getAmount();
                                enderpearl = item;
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

                    arrow.setMetadata("teleport", new FixedMetadataValue(plugin, player));
                    arrow.setMetadata("teleport_ride", new FixedMetadataValue(plugin, !player.isSneaking()));


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

                    }.runTaskTimer(plugin, 0L, 5L);

                    if (!player.isSneaking()) {

                        Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(arrow.getUniqueId().toString().trim().replaceAll("-", "").substring(0, 15));
                        team.addEntry(arrow.getUniqueId().toString());

                        team.addEntry(player.getName());

                        List<Entity> passengers = player.getPassengers();

                        passengers.forEach(entity -> {
                            if (e instanceof Player p) {
                                team.addEntry(p.getName());
                            } else {
                                team.addEntry(entity.getUniqueId().toString());
                            }
                        });

                        team.setAllowFriendlyFire(false);

                        teamMap.put(arrow.getUniqueId(), team);
                        ignoreEntityHit.put(arrow, player);


                        if (player.getVehicle() instanceof Arrow oldArrow) {
                            removeArrow(oldArrow);
                        }

                        arrow.addPassenger(player);

                        passengers.forEach(player::addPassenger);
                    }
                }
            }
        }
    }


}
