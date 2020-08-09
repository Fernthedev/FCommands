package com.github.fernthedev.fcommands.spigotclass.misc;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class WaterBoots implements Listener, CommandExecutor {
    //Another cool idea from Ender
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    private static void checkarmor() {
        Bukkit.getLogger().info("LOADED WATER WALKING EVENT");
            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
               // for (ItemStack stack : p.getPlayer().getInventory().getArmorContents()) {
                   // if (stack.getType() != null) {
                        ItemStack Boots = p.getInventory().getBoots();
                        if (Boots != null) {
                            if (Boots.getType() == Material.DIAMOND_BOOTS || Boots.getType() == Material.IRON_BOOTS) {
                                if ((Boots.getItemMeta().getLore() != null) && (Boots.getItemMeta().getLore().contains("WaterWalk"))) {
                                    Location pl = p.getLocation();
                                    Material m = p.getLocation().getBlock().getType();
                                    pl.setY(pl.getY() - 1);
                                    Material plbelow = pl.getBlock().getType();
                                    p.sendMessage("EXPERIMENTAL WATER");
                                        while (plbelow == Material.WATER) {
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1, 255, true, false));

                                    //}
                              //  }
                            }
                        }
                    }
                }
            }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack main = player.getInventory().getItemInMainHand();
            //ItemStack offhand = (ItemStack) player.getInventory().getBoots();
           // ItemStack inv = (ItemStack) player.getInventory();
            /* //if(((FernMain.getType() == Material.DIAMOND_BOOTS) && (offhand.getType() == Material.ICE)) || ((FernMain.getType() == Material.ICE) && (offhand.getType() == Material.DIAMOND_BOOTS)) || ((FernMain.getType() == Material.IRON_BOOTS) && (offhand.getType() == Material.ICE)) || ((FernMain.getType() == Material.ICE) && (offhand.getType() == Material.IRON_BOOTS))) { */
            if(main.getType() == Material.DIAMOND_BOOTS && player.getInventory().contains(Material.ICE) || main.getType() == Material.IRON_BOOTS && player.getInventory().contains(Material.ICE)) {
                ItemMeta mainlore = main.getItemMeta();
                ArrayList<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "WaterWalk");
                mainlore.setLore(lore);
                main.setItemMeta(mainlore);
                player.getInventory().remove(Material.ICE);
               /* } else if (offhand.getType() == Material.ICE) {
                    ItemMeta mainl = FernMain.getItemMeta();
                    ArrayList<String> mainlore = new ArrayList<String>();
                    mainlore.add("Waterfall");
                    mainl.setLore(mainlore);
                    player.getInventory().getItemInMainHand().setType(Material.AIR);
                }*/

                player.sendMessage("Successfully enchanted boots with WaterWalk");


            }else if(main.getType() != Material.DIAMOND_BOOTS || main.getType() != Material.IRON_BOOTS){
                player.sendMessage("This is not applicable for WaterWalk");
            }else if(!player.getInventory().contains(Material.ICE)) {
                player.sendMessage("You need ice to be able to enchant this");
            }
        }
        // If the player (or console) uses our NameColor correct, we can return true
        return true;
    }


    public static void onEnable() {
        checkarmor();
        Bukkit.getLogger().info("WATER WALKING HAS BEEN ENABLED!");
    }
}
