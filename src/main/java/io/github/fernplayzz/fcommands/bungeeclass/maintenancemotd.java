package io.github.fernplayzz.fcommands.bungeeclass;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class maintenancemotd implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler
    private void startMaintenancec(ProxyPingEvent eping) {
        ProxyServer getProxy = ProxyServer.getInstance();
                ServerPing pingResponse = eping.getResponse();
                PendingConnection address = eping.getConnection();
                pingResponse.setDescription(ChatColor.RED + "SERVER UNDER MAINTENANCE!");
                eping.setResponse(pingResponse);
    }

    public void startMaintenance() {
        ProxyPingEvent proxyPingEvent = null ;
        startMaintenancec(proxyPingEvent);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    private void stopMaintenancec(ProxyPingEvent eping) {
        ProxyServer getProxy = ProxyServer.getInstance();
        ServerPing pingResponse = eping.getResponse();
        PendingConnection address = eping.getConnection();
        pingResponse.setDescription(null);
        eping.setResponse(pingResponse);
    }

    public void stopMaintenance() {
        ProxyPingEvent proxyPingEvent = null ;
        stopMaintenancec(proxyPingEvent);
    }
}
