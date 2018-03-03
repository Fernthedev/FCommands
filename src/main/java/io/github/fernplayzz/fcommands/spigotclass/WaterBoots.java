package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

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

        public static void bootrecipe() {
            Bukkit.getLogger().info("LOADED RECIPES FOR WATERBOOTS");
            // Our custom variable which we will be changing around.
            ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
            ItemStack itemiro = new ItemStack(Material.IRON_BOOTS);
            ItemStack itemchai = new ItemStack(Material.CHAINMAIL_BOOTS);


            // The meta of the diamond sword where we can change the name, and properties of the item.
            ItemMeta meta = item.getItemMeta();
            ItemMeta metairo = itemiro.getItemMeta();
            ItemMeta metachai= itemchai.getItemMeta();

            // This sets the name of the item.
// Instead of the § symbol, you can use ChatColor.<color>
            meta.setDisplayName(item.getItemMeta().getDisplayName());
            metairo.setDisplayName(itemiro.getItemMeta().getDisplayName());
            metachai.setDisplayName(itemchai.getItemMeta().getDisplayName());
// Set the meta of the sword to the edited meta.
            ///DIA
            ItemMeta itemmeta = item.getItemMeta();
            ArrayList<String> lore = new ArrayList<String>();
            lore.add("WaterWalk");
            itemmeta.setLore(lore);
            //IRON
            ItemMeta itemmetairo = itemiro.getItemMeta();
            ArrayList<String> loreiro = new ArrayList<String>();
            loreiro.add("WaterWalk");
            itemmetairo.setLore(loreiro);

            //CHAIN

            ItemMeta itemmetachai = itemchai.getItemMeta();
            ArrayList<String> lorechai = new ArrayList<String>();
            lorechai.add("WaterWalk");
            itemmetachai.setLore(lorechai);


            // Create our custom recipe variable
            //DIAMOND //////////////////////////////////////////////////
            ShapedRecipe recipedia = new ShapedRecipe(item);
// Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
            recipedia.shape(" E ", " S ");

// Set what the letters represent.
            recipedia.setIngredient('E', Material.DIAMOND_BOOTS);
            recipedia.setIngredient('S', Material.ICE);

// Finally, add the recipe to the bukkit recipes
            Bukkit.addRecipe(recipedia);
            /////////////////////////////////////////////////////////////////
            //IRON //////////////////////////////////////////////////////////
            ShapedRecipe recipeiro = new ShapedRecipe(itemiro);

// Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
            recipeiro.shape(" E ", " S ");

// Set what the letters represent.
            recipeiro.setIngredient('E', Material.IRON_BOOTS);
            recipeiro.setIngredient('S', Material.ICE);

// Finally, add the recipe to the bukkit recipes
            Bukkit.addRecipe(recipeiro);
          //////////////////////////////////////////////////////////////
            //CHAIN
            ShapedRecipe recipechai = new ShapedRecipe(itemchai);

// Here we will set the places. E and S can represent anything, and the letters can be anything. Beware; this is case sensitive.
            recipeiro.shape(" E ", " S ");

// Set what the letters represent.
            recipechai.setIngredient('E', Material.IRON_BOOTS);
            recipechai.setIngredient('S', Material.ICE);

// Finally, add the recipe to the bukkit recipes
            Bukkit.addRecipe(recipechai);

        }

        public static void onEnable() {
        Bukkit.getLogger().info("WATER WALKING HAS BEEN ENABLED!");
        }
    }
