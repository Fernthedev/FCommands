package io.github.fernplayzz.fcommands.bungeeclass;

import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class servermaintenance extends Plugin implements Listener {
    @EventHandler
    public void lobbydisconnect(ServerDisconnectEvent disco) {
        if(disco.getTarget().getName().equals(getProxy().getServerInfo("Lobby").getName())) {
            getProxy().getLogger().info("Lobby was disconnected, enabling maintenance");
            getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
        }
    }
    @EventHandler
    public void lobbyconnect(ServerConnectEvent connect) {
        if(connect.getTarget().getName().equals(getProxy().getServerInfo("Lobby").getName())) {
            getProxy().getLogger().info("Lobby was connnected, disabling maintenance");
            getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
        }
    }


    @EventHandler
    public void lobbyconnected(ServerConnectedEvent connecte) {
        if(connecte.getServer().equals(getProxy().getServerInfo("Lobby").getName())) {
            getProxy().getLogger().info("Lobby was connnected, disabling maintenance");
            getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
        }
    }

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





    @Override
    public void onEnable() {
        getProxy().getLogger().info("ENABLED LOBBY MAINTENANCE DETECTOR");
    }




}




