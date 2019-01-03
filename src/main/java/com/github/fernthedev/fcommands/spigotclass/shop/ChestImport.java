package com.github.fernthedev.fcommands.spigotclass.shop;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fcommands.spigotclass.FilesManager;
import com.github.fernthedev.fcommands.spigotclass.interfaces.FCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This is used for importing the shop from <a href="https://www.spigotmc.org/resources/guishop.2451/">GuiShop</a>
 * This isn't loaded for default.
 */
public class ChestImport extends FCommand implements Listener, CommandExecutor {

    private static File chestConfigFile = new File(FernCommands.getInstance().getDataFolder(), "thing.yml");

    public void readFile(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        /*
          '7':
- id: '48:0'
- slot: 6
- name: ''
- buy-price: 60.0
- sell-price: 10.0
- enchantments: ''
- qty: 1
- type: ITEM
         */

        Integer index = 0;
        try {
            config.load(file);
            for (String childSectionName : config.getKeys(false)) {

                index += 1;

                getLogger().info("At key " + childSectionName);
                ConfigurationSection section = config.getConfigurationSection(childSectionName);
                List<Map<?, ?>> sectionSet = config.getMapList(childSectionName);



                //for(String keys : section.getKeys(false)) {
                //    getLogger().info( "Parent Section " + section.getName()  + " with a key "+ keys);
                //}

                /*
                if (section == null) {
                    section = config.getConfigurationSection("\'" +index.toString()+"\'");
                    getLogger().info("The section " + childSectionName + " is empty in " + childSectionName + " " + index);
                    childSectionName = index.toString();
                    if(section == null) {
                        getLogger().info("The section " + childSectionName + " is empty in " + childSectionName + " " + index);
                        fail = true;
                    }
                }*/
                getLogger().info("Child Section " + childSectionName + childSectionName + " with a key " + childSectionName);

                getLogger().info("Parent Section " + section + " with a key " + childSectionName);




               /* List<Map<?, ?>> sectionSet = config.getMapList(childSectionName);

                if (sectionSet == null || sectionSet.isEmpty()) {
                    sectionSet = config.getConfigurationSection(childSectionName).getMapList(childSectionName);
                    if (sectionSet == null || sectionSet.isEmpty()) {
                        getLogger().info("Section is empty " + config.getConfigurationSection(childSectionName).getStringList(childSectionName));
                    }
                }*/

                getLogger().info("Child Section is " + childSectionName + "." + childSectionName + " with parent " + section + " child " + section);
/*
                if (sectionSet != null && !sectionSet.isEmpty())
                    for (Map<?, ?> key : sectionSet) {
                        getLogger().info("Child Section " + childSectionName + childSectionName + " with a key " + key);
                    }
*/
                String[] splitted;
                String id = null;// = sectionSet.get("id");
                String data = null;

                Integer idInt = 0;

                Object slot;// = section.getInt("slot");
                String name;// = section.getString("name");
                double buyprice;// = section.getInt("buy-price");
                double sellprice;// = section.getInt("sell-price");
                String enchantments;// = section.getString("enchantments");
                int qty;// = section.getInt("qty");
                String type;// = section.getString("type");

                for (Map<?, ?> map : sectionSet) {
                    getLogger().info("Child Section " + childSectionName + childSectionName + " with a key " + index);

                    if (map.containsKey("id")) {
                        id = (String) map.get("id");

                        if(id != null) {
                            String[] split = id.split(Pattern.quote(":"));
                            if(split.length > 1) {
                                id = split[0];

                                try {
                                    idInt = Integer.parseInt(id);
                                } catch (NumberFormatException e) {}

                                data = split[1];
                            }
                        }
                    }

                    if (map.containsKey("slot")) {
                        slot = map.get("slot");

                    }

                    if (map.containsKey("name")) {
                        name = (String) map.get("name");
                    }

                    if (map.containsKey("buy-price")) {
                        Object buyPriceE = map.get("buy-price");

                        if(buyPriceE instanceof String) {
                            try {
                                buyprice = Double.parseDouble((String) buyPriceE);
                            } catch (NumberFormatException e) {
                            }
                        }

                        if(buyPriceE instanceof Double) {
                            buyprice = (double) buyPriceE;
                        }
                    }
                    if (map.containsKey("sell-price")) {
                        Object buyPriceE = map.get("sell-price");

                        if(buyPriceE instanceof String) {
                            try {
                                sellprice = Double.parseDouble((String) buyPriceE);
                            } catch (NumberFormatException e) {
                            }
                        }

                        if(buyPriceE instanceof Double) {
                            sellprice = (double) buyPriceE;
                        }
                    }

                    if (map.containsKey("enchantments")) {
                        name = (String) map.get("enchantments");
                    }
                    if (map.containsKey("qty")) {
                        Object qtyAmount = map.get("qty");

                        if(qtyAmount instanceof String) {
                            try {
                                qty = Integer.parseInt((String) qtyAmount);
                            } catch (NumberFormatException e) {
                            }
                        }

                        if(qtyAmount instanceof Integer) {
                            qty = (int) qtyAmount;
                        }
                    }
                    if (map.containsKey("type")) {
                        type = (String) map.get("type");
                    }

                }

                if(id != null && idInt.toString().equals(id)) {


                    Material material = Material.getMaterial(idInt);

                    if(material == null) {
                        getLogger().info("Why is material null? " + id);
                    }else {

                        ItemStack itemStack = new ItemStack(material);
                        Map<String, String> newThing = new HashMap<>();
                        if(data != null) {
                            newThing.put("data", data);
                            sectionSet.add(newThing); // .set("id", material.name());
                        }

                        newThing = new HashMap<>();
                        newThing.put("id",material.name());
                        sectionSet.add(newThing);
                        //section.set("ItemStack", itemStack);
                        config.set(childSectionName, sectionSet);
                    }
                }
            }
            /*
                                  Stone brick:
    MenuItem:
    - 'lore:&cBuy: $120.0#&aSell: $45.0'
    - name:Stone Brick
    - amount:1
    - type:SMOOTH_BRICK
    RewardType: Item
    Reward:
    - - amount:1
      - type:SMOOTH_BRICK
    PriceType: Money
    Price: 120.0
    Message: '&eYou bought %itemname%&e! Money left: &c%left%'
    ExtraPermission: ''
    InventoryLocation: 1
             */
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        } finally {
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {


            if (args.length == 0) {
                if (!(sender instanceof ConsoleCommandSender)) {
                    Player player = (Player) sender;
                    Block chestBlock = player.getTargetBlock(null, 5);

                    if (chestBlock instanceof Chest || chestBlock instanceof DoubleChest || chestBlock.getLocation().getBlock().getType() == Material.CHEST) {
                        BlockState chestState = chestBlock.getState();

                        Chest chest = (Chest) chestState;
                        Inventory inventory = chest.getInventory();
                        if (inventory instanceof DoubleChestInventory) {
                            DoubleChest doubleChest = (DoubleChest) inventory.getHolder();

                            FileConfiguration config = FilesManager.getChestConfig();
                            try {
                                FilesManager.getInstance().reloadConfig("chest");
                            } catch (IOException | InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                            for (int i = 0; i < doubleChest.getInventory().getSize(); i++) {
                                ItemStack itemStack = doubleChest.getInventory().getItem(i);


                                if (itemStack != null && itemStack.getType() != Material.AIR) {

                                    ItemMeta meta = itemStack.getItemMeta();

                                    List<String> lores = meta.getLore();

                                    if (lores == null || lores.isEmpty()) {
                                        getLogger().info("The item at slot " + i + " has no lore");
                                        sender.sendMessage("The item at slot " + i + " does not have price or name.");
                                    } else {

                                        String buyValue = lores.get(0);
                                        String sellValue = null;
                                        if (lores.size() > 1) sellValue = lores.get(1);
                                        String nameValue = null;
                                        if (lores.size() > 2) nameValue = lores.get(2);


                                        if ((buyValue == null || buyValue.contains("[a-zA-Z]+")) || (sellValue == null || sellValue.contains("[a-zA-Z]+"))) {
                                            sender.sendMessage("The price cannot be text at slot " + i + " item is " + itemStack.getItemMeta().getDisplayName());
                                        } else {

                                            if (nameValue == null) {
                                                //sender.sendMessage("The price cannot be text at slot " + i + " item is " + itemStack.getItemMeta().getDisplayName());
                                                nameValue = itemStack.getItemMeta().getDisplayName();
                                            } else {

                                                ConfigurationSection section = config.getConfigurationSection(nameValue);
                                                if (section != null) {
                                                    config.set(nameValue, null);
                                                    config.createSection(nameValue);
                                                    section = config.getConfigurationSection(nameValue);
                                                }

                                                if (section == null) {
                                                    config.createSection(nameValue);
                                                    section = config.getConfigurationSection(nameValue);
                                                }

                                                double buyIntPrice = Integer.parseInt(buyValue);
                                                double sellIntPrice = Integer.parseInt(sellValue);

                                                List<String> menuItem = new ArrayList<>();

                                                menuItem.add("\'lore:'&cBuy: $" + buyIntPrice + "#&aSell: $" + sellIntPrice + "\'");
                                                menuItem.add("name: " + nameValue);
                                                menuItem.add("amount: " + itemStack.getType());
                                                section.set("MenuItem", menuItem);
                                                section.set("RewardType", "Item");
                                                List<String> rewads = new ArrayList<>();
                                                rewads.add("- amount:1");
                                                rewads.add("type:" + itemStack.getType());
                                                section.set("Reward", rewads);

                                                section.set("PriceType", "Money");
                                                section.set("Price", buyIntPrice);
                                                section.set("Message", "&aYou bought %itemname%&a! Money Left: &c%left%");
                                                section.set("ExtraPermission", null);
                                                section.set("InventoryLocation: ", i);
                                            }
                                        }
                                    }
                                }
                    /*
                      Stone brick:
    MenuItem:
    - 'lore:&cBuy: $120.0#&aSell: $45.0'
    - name:Stone Brick
    - amount:1
    - type:SMOOTH_BRICK
    RewardType: Item
    Reward:
    - - amount:1
      - type:SMOOTH_BRICK
    PriceType: Money
    Price: 120.0
    Message: '&eYou bought %itemname%&e! Money left: &c%left%'
    ExtraPermission: ''
    InventoryLocation: 1
                     */


                            }
                            try {
                                config.save(FilesManager.getChestConfigFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            sender.sendMessage(ChatColor.GREEN + "Successfully set the shop");
                            // You have a double chest instance
                        } else {
                            sender.sendMessage("Not a double chest.");
                        }
                    } else {
                        sender.sendMessage("Please look at a chest" + player.getTargetBlock(null, 5).getType());
                    }
                }
            } else {
                String type = args[0];
                if (type.equalsIgnoreCase("readfile")) {
                    sender.sendMessage(ChatColor.AQUA + "Reading...");
                    readFile(chestConfigFile);
                    sender.sendMessage(ChatColor.GREEN + "Finished.");
                    return true;
                }
                if (args.length > 1) {
                    if (!(sender instanceof ConsoleCommandSender)) {
                    String secondArg = args[1];
                        Player player = (Player) sender;
                        ItemStack item = player.getInventory().getItemInMainHand();
                        switch (type.toLowerCase()) {


                            case "setbuy":

                                if (secondArg.contains("[a-zA-Z]+")) {
                                    sender.sendMessage("The price cannot be text");
                                    return true;
                                }


                                if (item != null && (item.getType() != Material.AIR)) {
                                    ItemMeta meta = item.getItemMeta();

                                    List<String> lores = meta.getLore();
                                    if (lores.get(0) != null)
                                        lores.set(0, secondArg);
                                    else
                                        lores.add(secondArg);

                                    meta.setLore(lores);
                                    item.setItemMeta(meta);
                                    sender.sendMessage("Successfully set price to " + secondArg);
                                } else {
                                    sender.sendMessage("No item?");
                                }

                                break;
                            case "setsell":
                                if (secondArg.contains("[a-zA-Z]+")) {
                                    sender.sendMessage("The price cannot be text");
                                    return true;
                                }

                                if (item != null && (item.getType() != Material.AIR)) {
                                    ItemMeta meta = item.getItemMeta();

                                    List<String> lores = meta.getLore();

                                    if (lores.get(1) != null)
                                        lores.set(1, secondArg);
                                    else
                                        lores.add(secondArg);

                                    meta.setLore(lores);
                                    item.setItemMeta(meta);
                                    sender.sendMessage("Successfully set price to " + secondArg);
                                } else {
                                    sender.sendMessage("No item?");
                                }

                                break;
                            case "setname":
                                if (item != null && (item.getType() != Material.AIR)) {
                                    ItemMeta meta = item.getItemMeta();

                                    List<String> lores = meta.getLore();

                                    if (lores.get(2) != null)
                                        lores.set(2, secondArg);
                                    else
                                        lores.add(secondArg);

                                    meta.setLore(lores);
                                    item.setItemMeta(meta);
                                    sender.sendMessage("Successfully set name to " + secondArg);
                                } else {
                                    sender.sendMessage("No item?");
                                    return true;
                                }

                                break;
                            default:
                                sender.sendMessage("No such argument. Only: setsell,setname and setbuy");
                                break;
                        }
                    }else {
                        sender.sendMessage("Not a player ;-;");
                    }
                }else{
                    sender.sendMessage("Not enough arguments");
                }
            }
        return true;
    }
}
