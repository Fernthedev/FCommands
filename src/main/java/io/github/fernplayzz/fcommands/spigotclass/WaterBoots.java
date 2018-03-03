package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WaterBoots implements Listener {
    //Another cool idea from Ender
    public static void checkarmor() {
        Bukkit.getLogger().info("LOADED WATER WALKING EVENT");
        for (Bukkit.getOnlinePlayers(); ;) {
            Player p = (Player) Bukkit.getOnlinePlayers();
            ItemStack Boots = p.getInventory().getBoots();
            if (Boots != null) {
                if (Boots.getType() == Material.DIAMOND_BOOTS || Boots.getType() == Material.IRON_BOOTS || Boots.getType() == Material.LEATHER_BOOTS) {
                    if ((Boots.getItemMeta().getLore() != null) &&
                            (Boots.getItemMeta().getLore().contains("WaterWalk"))) {
                                Material m = p.getLocation().getBlock().getType();
                        for(; ;) {
                            if(m == Material.STATIONARY_WATER || m == Material.WATER ) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1, 255, true, false));
                            }
                        }
                        }
                    }
                }

            }
        }
        public static void onEnable() {
        Bukkit.getLogger().info("WATER WALKING HAS BEEN ENABLED!");
        }
    }
