package io.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class servermaintenance implements Listener {

    @SuppressWarnings({"deprecation", "ConstantConditions"})
    private boolean lobbycheck() {
        ProxyServer getProxy = ProxyServer.getInstance();
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
            return online;
        } else if (!online) {
            getProxy.getLogger().info("Lobby was disconnected, enabling maintenance");
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
                new FileManager().loadFiles("config",true);
            } catch (IOException e) {
                FernCommands.getInstance().getLogger().warning("Unable to load config");
            }
            Configuration config = new FileManager().getConfig();
            ServerPing pingResponse = eping.getResponse();
            //PendingConnection address = eping.getConnection();
            Object motd = config.get("Motd");
            if (motd == null) {
                ProxyServer.getInstance().getLogger().warning("Unable to find MOTD");
            } else {
                //String motd = config.getString("Motd");
                String emotd;
                emotd = motd.toString();
                if(emotd == null) {
                    FernCommands.getInstance().getLogger().warning("Motd is null oh no!");
                }
                //ChatColor.translateAlternateColorCodes('&', (String) motd);
                motd = ((String) motd).replace('&','§');
                pingResponse.setDescription((String) motd);
                eping.setResponse(pingResponse);
            }
        }else {
            ServerPing pingResponse = eping.getResponse();
            //PendingConnection address = eping.getConnection();
            pingResponse.setDescription(ChatColor.RED + "SERVER UNDER MAINTENANCE!");
            eping.setResponse(pingResponse);
        }
    }
}




