package com.github.fernthedev.fcommands.bungee;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.ServerMaintenance;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class BungeeServerMotdPing implements Listener {

    @EventHandler
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (FileManager.getConfigValues().getUseMotd() && !ServerMaintenance.isOnline()) {
            ServerPing pingResponse = eping.getResponse();


            pingResponse.setDescriptionComponent(message(FileManager.getConfigValues().getOfflineServerMotd()));
            eping.setResponse(pingResponse);
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}
