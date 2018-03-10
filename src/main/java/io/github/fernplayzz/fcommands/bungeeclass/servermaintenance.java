package io.github.fernplayzz.fcommands.bungeeclass;

import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public class servermaintenance extends Plugin implements Listener {
    public void lobbycheck() {
       /* boolean online = false; {
            try {
            Socket s = new Socket();
                s.connect(new InetSocketAddress("localhost", 25566), 20); //good timeout is 10-20
                s.close();
                getProxy().getLogger().info("Lobby connected, disabling maintenance");
                // ONLINE
                online = true;
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
            } catch (UnknownHostException e) {
                // OFFLINE
                getProxy().getLogger().info("Lobby disconnected, enabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
            } catch (IOException e) {
                e.printStackTrace();
                // OFFLINE
                getProxy().getLogger().info("Lobby disconnected, enabling maintenance");
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
            }*/


        /*if (online) {
            //Bukkit.dispatchCommand(sender, cmd);
            getProxy().getLogger().info("Lobby connected, disabling maintenance");
            getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
        }*/
    }


    public void onEnable() {
        lobbycheck();
        getProxy().getLogger().info("ENABLED LOBBY MAINTENANCE DETECTOR");
    }




}




