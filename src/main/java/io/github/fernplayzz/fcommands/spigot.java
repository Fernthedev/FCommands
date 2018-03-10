package io.github.fernplayzz.fcommands;

import io.github.fernplayzz.fcommands.spigotclass.BedFire;
import io.github.fernplayzz.fcommands.spigotclass.LavaBurn;
import io.github.fernplayzz.fcommands.spigotclass.RidePlayer;
import io.github.fernplayzz.fcommands.spigotclass.WaterBoots;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class spigot extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getLogger().info("Hey! This is just for you to know that the plugin is enabled!");
        //BedFire.run();
        //Bukkit.getServer().getPluginManager().registerEvents(new BedFire(), this);
        this.getServer().getPluginManager().registerEvents(new BedFire(), this);
        this.getServer().getPluginManager().registerEvents(new LavaBurn(), this);
        BedFire.onEnable();
        LavaBurn.onEnable();
        this.getServer().getPluginManager().registerEvents(new WaterBoots(), this);
        WaterBoots.onEnable();
        this.getCommand("fboot").setExecutor(new WaterBoots());
        this.getServer().getPluginManager().registerEvents(new RidePlayer(), this);
        RidePlayer.onEnable();
        //BedFire bedfire = new BedFire();
        //this.getCommand("fbedcheck").setExecutor(new BedFire());
        /*if (getServer().getPluginManager().isPluginEnabled(bedfire)) {
            getLogger().info("UNABLE TO START BEDFIRE!");
        }else{
            getLogger().info("ENABLED BEDFIRE!");
        }*/

    }


}
