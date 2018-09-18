package io.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class servermaintenance implements Listener {

    private boolean onlineCheck() {
        boolean online;
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
                    FernCommands.getInstance().getLogger().info("Unable to close socket (ignore this)");
                }
        }
        return online;
    }

    @SuppressWarnings({"deprecation", "ConstantConditions"})
    private boolean lobbycheck() {
        ProxyServer getProxy = ProxyServer.getInstance();
        boolean online = onlineCheck();

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
    public void maintenanceMOTD(ProxyPingEvent eping) {
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
            String motd = config.getString("Motd");
            if (motd == null) {
                ProxyServer.getInstance().getLogger().warning("Unable to find MOTD");
            } else {
                //String motd = config.getString("Motd");
                String emotd;
                emotd = motd;
                if(emotd.isEmpty()) {
                    FernCommands.getInstance().getLogger().warning("Motd is null oh no!");
                }

                motd = motd.replace("\\n", "\n");

                pingResponse.setDescriptionComponent(message(motd));
                eping.setResponse(pingResponse);
            }
        }else {
            ServerPing pingResponse = eping.getResponse();
            //PendingConnection address = eping.getConnection();
            pingResponse.setDescriptionComponent(message("&cSERVER UNDER MAINTENANCE!"));
            eping.setResponse(pingResponse);
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}




