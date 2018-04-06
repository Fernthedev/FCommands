package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class spigot extends JavaPlugin implements Listener {
    private boolean useMcMMO;

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
        if(this.getServer().getPluginManager().isPluginEnabled("Multiverse-Core")) {
            getLogger().info("Found Multiverse, checking to see skylands are enabled");
            if(this.getServer().getPluginManager().isPluginEnabled("SB-Skylands")) {
                getLogger().info("Found skylands, enabling enderpearl and overworld fall");
                this.getServer().getPluginManager().registerEvents(new skylands(), this);
            }
        }

        if(Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO")) {
            hook();
                Bukkit.getServer().getPluginManager().isPluginEnabled("NametagEdit");
                {
                    getLogger().info("FOUND MCMMO AND NAMETAGEDIT IN PLUGINS, ENABLING AUTO RELOAD");
                    this.getServer().getPluginManager().registerEvents(new NTEmcMMO(), this);
                }
            }
        this.getServer().getPluginManager().registerEvents(new godpearl(), this);
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
    public boolean mcmmoEnabled() {
        return useMcMMO;
    }
    public void hook()
    {
        useMcMMO = Bukkit.getServer().getPluginManager().isPluginEnabled("McMMO");
    }


    protected void infolog(String text) {
        if (!text.isEmpty()) {
            getLogger().info(text);
        }
    }


}
