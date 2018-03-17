package io.github.fernplayzz.fcommands;


import net.md_5.bungee.api.plugin.Plugin;
import io.github.fernplayzz.fcommands.bungeeclass.*;

public class bungee extends Plugin {
   // @Override
   // public void onEnable() {
     //   getLogger().info("BUNGEECORD IS NOT SUPPORTED YET!");
       // getLogger().info("DISABLING PLUGIN!");
       // getProxy().getPluginManager().unregisterListeners(this);
       // getProxy().getPluginManager().unregisterCommands(this);
        //this.onDisable();
    //}*/
    @Override
   public void onEnable() {
        getLogger().info("ENABLED FERNCOMMANDS FOR BUNGEECORD");
    getProxy().getPluginManager().registerListener(this, new servermaintenance());
    }

}
