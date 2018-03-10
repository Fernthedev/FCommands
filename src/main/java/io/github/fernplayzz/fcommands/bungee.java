package io.github.fernplayzz.fcommands;


import net.md_5.bungee.api.plugin.Plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
      // getProxy().getPluginManager().registerListener(this, new servermaintenance());
       servermaintenance();
    }



    public void servermaintenance() {
        getProxy().getLogger().info("LOADED LOBBY MAINTENANCE DETECTOR");
        for(; ;) {
            boolean online = false;
            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress("ADRESS", 25566), 20); //good timeout is 10-20
                // ONLINE
                getProxy().getLogger().info("Lobby connected, disabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
                s.close();
                getProxy().getLogger().info("Lobby connected, disabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
                online = true;
            } catch (UnknownHostException e) {
                getProxy().getLogger().info("Lobby disconnected, enabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
                // OFFLINE
            } catch (IOException e) {
                // OFFLINE
                getProxy().getLogger().info("Lobby disconnected, enabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
            }
        }
    }
}
