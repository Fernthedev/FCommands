package io.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class servermaintenance implements Listener {
    private final ProxyServer getProxy = ProxyServer.getInstance();

    //boolean online = false;
    //@EventHandler
    //public void lobbydisconnect(ServerDisconnectEvent disco) {
   //     lobbycheck();
  //  }
   /* @EventHandler
    public void lobbyconnect(ServerConnectEvent connect) {
        if() {
            getProxy().getLogger().info("Lobby was connnected, disabling maintenance");
            getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance off");
        }
    }*/


    /*@EventHandler
    public void lobbyconnected(ServerConnectedEvent connecte) {
        lobbycheck();

       /* boolean online = false;
        try {
            //Socket s = new Socket("localhost", 25566);
            Socket s = new Socket();
            s.connect(new InetSocketAddress("localhost", 25566), 20); //good timeout is 10-20
            // ONLINE
            s.close();
            online = true;
        } catch (UnknownHostException e) {
            // OFFLINE
          //  getProxy().getLogger().info("Lobby was disconnected, enabling maintenance");
          //  getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
        } catch (IOException e) {
            // OFFLINE
            //getProxy().getLogger().info("Lobby was disconnected, enabling maintenance");
            //getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "/maintenance on");
        }

        if(online) {
            getProxy.getLogger().info("Lobby was connected, disabling maintenance");
            getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "/maintenance off");
        }else{
            getProxy.getLogger().info("Lobby was disconnected, enabling maintenance");
            getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "/maintenance on");
        }

    }*/

    @EventHandler
    public void prelogin(PreLoginEvent prelogin) {
        //lobbycheck();
    }

    /*@EventHandler
    public void login(LoginEvent login) {
        lobbycheck();
    }*/

    /*@EventHandler
    public void serverconnect(ServerConnectedEvent connect) {
        lobbycheck();
    }*/

    public void onEnable() {
        ProxyServer getProxy = ProxyServer.getInstance();
        getProxy.getLogger().info("ENABLED LOBBY MAINTENANCE DETECTOR");
    }

    @SuppressWarnings({"deprecation", "ConstantConditions"})
    private boolean lobbycheck() {
        ProxyServer getProxy = ProxyServer.getInstance();
        //boolean online = false;
        /*getProxy.getServers().get(getProxy.getServerInfo("lobby").getName()).ping(new Callback<ServerPing>() {
            boolean online;

            {
                this.online = online;
            }

            @Override
            public void done(ServerPing result, Throwable error) {
                if (error != null) {
                    //Means that server is not responding : OFFLINE
                    online = false;
                    //Store this, by example, in a Hashmap<Server,Boolean> serverStatus, false is OFFLINE and true ONLINE
                   // Object put = hm.put(1, false);
                } else {
                    online = true;

                }
            }
        });*/
        boolean online;
        {
            Socket s = null;
            try {
                s = new Socket();
                s.connect(new InetSocketAddress("localhost", 25566), 20);
                online = true;
            } catch (Exception e) {
                online = false;
            } finally {
                if (s != null)
                    try {
                        s.close();
                    } catch (Exception ignored) {
                    }
            }
        }

        if (online) {
            getProxy.getLogger().info("Lobby was connected, disabling maintenance");
            //getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "/maintenance off");
            //getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "maintenance off");
            return online;
            // Bukkit.getServer().getPluginManager().callEvent(myEvent)
        } else if (!online) {
            getProxy.getLogger().info("Lobby was disconnected, enabling maintenance");
            //getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "/maintenance on");
            // getProxy.getPluginManager().dispatchCommand(getProxy.getConsole(), "maintenance on");
            return online;
        }
        return online;
    }
    @EventHandler
    @SuppressWarnings("deprecation")
    public void MaintenanceMotd(ProxyPingEvent eping) {
        boolean online = this.lobbycheck();
        if (online) {
            try {
                new bungee().loadFiles("config");
            } catch (IOException e) {
                getProxy.getLogger().warning("Unable to load config");
            }
            Configuration config = new bungee().getConfig();
            ServerPing pingResponse = eping.getResponse();
            PendingConnection address = eping.getConnection();
            Object motd = config.get("Motd");
            if (motd == null) {
                ProxyServer.getInstance().getLogger().warning("Unable to find MOTD");
            } else {
                //String motd = config.getString("Motd");
                String emotd;
                emotd = motd.toString();
                if(emotd == null) {
                    getProxy.getLogger().warning("Motd is null oh no!");
                }
                //ChatColor.translateAlternateColorCodes('&', (String) motd);
                motd = ((String) motd).replace('&','§');
                pingResponse.setDescription((String) motd);
                eping.setResponse(pingResponse);
            }
        }else {
            ServerPing pingResponse = eping.getResponse();
            PendingConnection address = eping.getConnection();
            pingResponse.setDescription(ChatColor.RED + "SERVER UNDER MAINTENANCE!");
            eping.setResponse(pingResponse);
        }
    }


}




