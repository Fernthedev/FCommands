package io.github.fernthedev.fcommands.spigotclass.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class namecolor implements CommandExecutor, Listener {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("namecolor") && sender.hasPermission("fernc.namecolor")) {
// Here we create our named help "inventory"
                Inventory namecolor = Bukkit.getServer().createInventory(p, 9, "NameColor");

                //Here you define our item
                ItemStack ref1 = new ItemStack(Material.WOOL,1,(byte)14);
                ItemMeta metaref1 = ref1.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();

                lore.add("test");

                metaref1.setLore(lore);
                metaref1.setDisplayName(message("&cExit"));

                ItemStack red = new ItemStack(Material.WOOL,1,(byte)14);
                red.getItemMeta().setDisplayName(message("&cRed Name"));
                ref1.setItemMeta(metaref1);
                namecolor.setItem(5, ref1);
                namecolor.setItem(6,red);


                //Here opens the inventory
                p.openInventory(namecolor);

            }
        }
        return false;
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {

        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getTitle().equalsIgnoreCase("NameColor")) {
            e.setCancelled(true);
            if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }


            if (e.getSlot() == 5 && (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(message("&cExit")))) {
                p.sendMessage(message("&cExited"));
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if (e.getSlot() == 6 && (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(message("&cRed Name")))) {
                p.sendMessage(message("&aSuccessful. Name is now &cred"));
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }


        }
    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}
