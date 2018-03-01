package io.github.fernplayzz.fcommands;


import net.md_5.bungee.api.plugin.Plugin;

public class bungee extends Plugin {
    @Override
    public void onEnable() {
        getLogger().info("BUNGEECORD IS NOT SUPPORTED YET!");
        getLogger().info("DISABLING PLUGIN!");
        getProxy().getPluginManager().unregisterListeners(this);
        getProxy().getPluginManager().unregisterCommands(this);
        this.onDisable();
    }
}
