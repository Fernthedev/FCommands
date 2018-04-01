package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class spigot extends JavaPlugin implements Listener {


    @Override
    public void onEnable() {
        getLogger().info("Hey! This is just for you to know that the plugin is enabled!");
        //BedFire.run();
        //Bukkit.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new BedFire(), this);
        //this.getServer().getPluginManager().registerEvents(new LavaBurn(), this);
        BedFire.onEnable();
        LavaBurn.onEnable();
        this.getServer().getPluginManager().registerEvents(new WaterBoots(), this);
        WaterBoots.onEnable();
        this.getCommand("fboot").setExecutor(new WaterBoots());
        if(this.getServer().getPluginManager().isPluginEnabled("mcMMO")) {
            this.getServer().getPluginManager().isPluginEnabled("NametagEdit");
            {
                this.getServer().getLogger().info("FOUND MCMMO AND NTE, ENABLING AUTO RELOAD");
                    this.getServer().getPluginManager().registerEvents(new NTEmcMMO(), this);
            }
        }
        //this.getServer().getPluginManager().registerEvents(new RidePlayer(), this);
        //RidePlayer.onEnable();
        //BedFire bedfire = new BedFire();
        //this.getCommand("fbedcheck").setExecutor(new BedFire());
        /*if (getServer().getPluginManager().isPluginEnabled(bedfire)) {
            getLogger().info("UNABLE TO START BEDFIRE!");
        }else{
            getLogger().info("ENABLED BEDFIRE!");
        }*/

    }


}
