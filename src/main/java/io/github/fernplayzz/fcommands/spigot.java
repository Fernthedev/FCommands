package io.github.fernplayzz.fcommands;

import io.github.fernplayzz.fcommands.spigotclass.BedFire;
import org.bukkit.plugin.java.JavaPlugin;

public class spigot extends JavaPlugin {
    public static spigot plugin;
    @Override
    public void onEnable() {
        getLogger().info("Hey! This is just for you to know that the plugin is enabled!");
        BedFire bedfire = new BedFire();
        BedFire.run();
        getServer().getPluginManager().registerEvents(bedfire, this);

    }


}
