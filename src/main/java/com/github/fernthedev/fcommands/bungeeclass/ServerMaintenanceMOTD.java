package com.github.fernthedev.fcommands.bungeeclass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServerMaintenanceMOTD implements Listener {

    private boolean onlineCheck() {
        boolean online = false;
        for(int port : FileManager.getConfigValues().getPortChecks()) {
            Socket s = null;
            try {
                s = new Socket();
                s.connect(new InetSocketAddress("localhost", port), 20);
                online = true;
            } catch (Exception e) {
                online = false;
                break;
            } finally {
                if (s != null)
                    try {
                        s.close();
                    } catch (Exception ignored) {
                        FernCommands.getInstance().getLogger().info("Unable to close socket (ignore this)");
                    }
            }
        }
        return online;
    }

    @SuppressWarnings({"deprecation", "ConstantConditions"})
    private boolean serverCheck() {
        ProxyServer getProxy = ProxyServer.getInstance();
        boolean online = onlineCheck();

        if (online) {
            getProxy.getLogger().info("Lobby was connected, disabling maintenance");
            return online;
        } else {
            getProxy.getLogger().info("Lobby was disconnected, enabling maintenance");
            return online;
        }
    }


    @EventHandler
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (FileManager.getConfigValues().isUseMotd()) {
            boolean online = this.serverCheck();
            if (online) {
                try {
                    new FileManager().loadFiles("config", true);
                } catch (IOException e) {
                    FernCommands.getInstance().getLogger().warning("Unable to load config");
                }
                ServerPing pingResponse = eping.getResponse();

                String motd = FileManager.getConfigValues().getMotd();
                if (motd == null) {
                    ProxyServer.getInstance().getLogger().warning("Unable to find MOTD");
                } else {

                    if (motd.isEmpty()) {
                        FernCommands.getInstance().getLogger().warning("Motd is null oh no!");
                        return;
                    }

                    motd = motd.replace("\\n", "\n");

                    pingResponse.setDescriptionComponent(message(motd));
                    eping.setResponse(pingResponse);
                }
            } else {
                ServerPing pingResponse = eping.getResponse();

                pingResponse.setDescriptionComponent(message(FileManager.getConfigValues().getOfflineServerMotd()));
                eping.setResponse(pingResponse);
            }
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}




