package io.github.fernthedev.fcommands.spigotclass.gui;

import com.nametagedit.plugin.NametagEdit;
import io.github.fernthedev.fcommands.spigotclass.spigot;
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
                Inventory namecolor = Bukkit.getServer().createInventory(p, 27, "NameColor");

                //Here you define our item
                ItemStack ref1 = new ItemStack(Material.BARRIER);
                ItemMeta metaref1 = ref1.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();

                //lore.add("test");

                metaref1.setLore(lore);
                metaref1.setDisplayName(message("&cExit"));
                ref1.setItemMeta(metaref1);

                //COLORS
                //DARK RED 251:14
                ItemStack darkred = new ItemStack(Material.CONCRETE,1,(byte)14);
                ItemMeta darkredmeta = darkred.getItemMeta();
                darkredmeta.setDisplayName(message("&4DarkRed Name"));
                darkred.setItemMeta(darkredmeta);
                //RED
                ItemStack red = new ItemStack(Material.WOOL,1,(byte)14);
                ItemMeta redmeta = red.getItemMeta();
                redmeta.setDisplayName(message("&cRed Name"));
                red.setItemMeta(redmeta);
                //ORANGE
                ItemStack orange = new ItemStack(Material.WOOL,1,(byte)1);
                ItemMeta orangemeta = orange.getItemMeta();
                orangemeta.setDisplayName(message("&6Orange Name"));
                orange.setItemMeta(orangemeta);
                //YELLOW
                ItemStack yellow = new ItemStack(Material.WOOL,1,(byte)4);
                ItemMeta yellowmeta = yellow.getItemMeta();
                yellowmeta.setDisplayName(message("&eYellow Name"));
                yellow.setItemMeta(yellowmeta);
                //LIME
                ItemStack lime = new ItemStack(Material.WOOL,1,(byte)5);
                ItemMeta limemeta = lime.getItemMeta();
                limemeta.setDisplayName(message("&aLime Name"));
                lime.setItemMeta(limemeta);
                //GREEN
                ItemStack green = new ItemStack(Material.WOOL,1,(byte)13);
                ItemMeta greenmeta = green.getItemMeta();
                greenmeta.setDisplayName(message("&2Green Name"));
                green.setItemMeta(greenmeta);
                //LIGHT BLUE
                ItemStack lightblue = new ItemStack(Material.WOOL,1,(byte)3);
                ItemMeta lightbluemeta = lime.getItemMeta();
                lightbluemeta.setDisplayName(message("&bLightBlue Name"));
                lightblue.setItemMeta(lightbluemeta);
                //BLUE 251:11
                ItemStack blue = new ItemStack(Material.CONCRETE,1,(byte)11);
                ItemMeta bluemeta = lime.getItemMeta();
                bluemeta.setDisplayName(message("&9Blue Name"));
                blue.setItemMeta(bluemeta);
                //CYAN
                ItemStack cyan = new ItemStack(Material.WOOL,1,(byte)9);
                ItemMeta cyanmeta = cyan.getItemMeta();
                cyanmeta.setDisplayName(message("&3Cyan Name"));
                cyan.setItemMeta(cyanmeta);
                //DARK BLUE
                ItemStack darkblue = new ItemStack(Material.WOOL,1,(byte)11);
                ItemMeta darkbluemeta = darkblue.getItemMeta();
                darkbluemeta.setDisplayName(message("&1DarkBlue Name"));
                darkblue.setItemMeta(darkbluemeta);
                //PURPLE
                ItemStack purple = new ItemStack(Material.WOOL,1,(byte)10);
                ItemMeta purplemeta = purple.getItemMeta();
                purplemeta.setDisplayName(message("&5Purple Name"));
                purple.setItemMeta(purplemeta);
                //MAGENTA
                ItemStack magenta = new ItemStack(Material.WOOL,1,(byte)2);
                ItemMeta magentameta = magenta.getItemMeta();
                magentameta.setDisplayName(message("&dMagenta Name"));
                magenta.setItemMeta(magentameta);
                //LIGHT GRAY
                ItemStack lightgray = new ItemStack(Material.WOOL,1,(byte)8);
                ItemMeta lightgraymeta = lightgray.getItemMeta();
                lightgraymeta.setDisplayName(message("&7LightGray Name"));
                lightgray.setItemMeta(lightgraymeta);
                //GRAY
                ItemStack gray = new ItemStack(Material.WOOL,1,(byte)7);
                ItemMeta graymeta = gray.getItemMeta();
                graymeta.setDisplayName(message("&8Gray Name"));
                gray.setItemMeta(graymeta);
                //BLACK
                ItemStack black = new ItemStack(Material.WOOL,1,(byte)15);
                ItemMeta blackmeta = black.getItemMeta();
                blackmeta.setDisplayName(message("&0Black Name"));
                black.setItemMeta(blackmeta);
                //WHITE
                ItemStack white = new ItemStack(Material.WOOL,1,(byte)0);
                ItemMeta whitemeta = white.getItemMeta();
                whitemeta.setDisplayName(message("&fWhite Name"));
                white.setItemMeta(whitemeta);


                namecolor.setItem(1, ref1);
                namecolor.setItem(9,darkred);
                namecolor.setItem(10,red);
                namecolor.setItem(11,orange);
                namecolor.setItem(12,yellow);
                namecolor.setItem(13,lime);
                namecolor.setItem(14,green);
                namecolor.setItem(15,lightblue);
                namecolor.setItem(16,blue);
                namecolor.setItem(17,cyan);
                namecolor.setItem(18,darkblue);
                namecolor.setItem(19,purple);
                namecolor.setItem(20,magenta);
                namecolor.setItem(21,lightgray);
                namecolor.setItem(22,gray);
                namecolor.setItem(23,black);
                namecolor.setItem(24,white);

                //Here opens the inventory
                p.openInventory(namecolor);

            }
        }
        return true;
    }

    @EventHandler
    private void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if (e.getInventory().getTitle().equalsIgnoreCase("NameColor")) {
            e.setCancelled(true);
            if ((e.getCurrentItem() == null) || (e.getCurrentItem().getType().equals(Material.AIR))) {
                return;
            }
            String currentslot = e.getCurrentItem().getItemMeta().getDisplayName();

            if (e.getSlot() == 1 && (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(message("&cExit")))) {
                p.sendMessage(message("&cExited"));
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&4DarkRed Name")))) {
                setPrefix(p,"&4");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if (e.getSlot() == 10 && (currentslot.equalsIgnoreCase(message("&cRed Name")))) {
                setPrefix(p,"&c");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&6Orange Name")))) {
                setPrefix(p,"&6");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&eYellow Name")))) {
                setPrefix(p,"&e");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&aLime Name")))) {
                setPrefix(p,"&a");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&2Green Name")))) {
                setPrefix(p,"&2");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&bLightBlue Name")))) {
                setPrefix(p,"&b");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&9Blue Name")))) {
                setPrefix(p,"&9");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&3Cyan Name")))) {
                setPrefix(p,"&3");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&4DarkBlue Name")))) {
                setPrefix(p,"&1");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&dPurple Name")))) {
                setPrefix(p,"&5");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&9Magenta Name")))) {
                setPrefix(p,"&d");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&7LightGray Name")))) {
                setPrefix(p,"&7");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&9Gray Name")))) {
                setPrefix(p,"&8");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&0Black Name")))) {
                setPrefix(p,"&0");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }
            if ((currentslot.equalsIgnoreCase(message("&fWhite Name")))) {
                setPrefix(p,"&f");
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
            }

        }
    }

    private void setPrefix(Player p,String color) {
        boolean fail = false;
        if(spigot.getInstance().isVaultEnabled() && spigot.getChat().isEnabled()) {
                spigot.getChat().setPlayerPrefix(p, message(color));
        }
        if(spigot.getInstance().isNTEEnabled()) {
            //if(!NametagEdit.getApi().getNametag(p).getPrefix().equals("") || !NametagEdit.getApi().getNametag(p).getPrefix().isEmpty()) {
                NametagEdit.getApi().setPrefix(p,color);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"nte player " + p.getName() + " prefix " + color + "");
                spigot.getInstance().getLogger().info("nte player " + p.getName() + " prefix \"" + color + "\"");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"nte reload");
                //NametagEdit.getApi().reloadNametag(p);
          //  }else{
          //      fail = true;
         //   }
        }
        spigot.getInstance().getLogger().info(" the length of prefix is " + spigot.getChat().getPlayerPrefix(p).length());
        //NametagEdit.getApi().setPrefix(p, color);
        //if(fail)
       //     p.sendMessage(message("&cYou already have a prefix, this will replace it. &6(Just put the color code after your prefix to make your name that color)"));
      //  if(!fail)
        p.sendMessage(message("&aSuccessful. Name is now " + color + "this color"));
    }

    private String message(String message) {
        return ChatColor.translateAlternateColorCodes('&',message);
    }
}
