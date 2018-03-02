package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SubMain extends JavaPlugin implements Listener {
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new BedFire(), this);
    }
}
