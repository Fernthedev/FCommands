package com.github.fernthedev.fcommands.bungee;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.ServerMaintenance;
import com.github.fernthedev.fcommands.proxy.WhichFile;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.modules.ProxyFile;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import javax.inject.Inject;

public class BungeeServerMotdPing implements Listener {

    @Inject
    @ProxyFile(WhichFile.CONFIG)
    private Config<ConfigValues> config;

    @Inject
    private ServerMaintenance serverMaintenance;

    @EventHandler
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (config.getConfigData().getUseMotd() && !serverMaintenance.isOnline()) {
            ServerPing pingResponse = eping.getResponse();


            pingResponse.setDescriptionComponent(message(config.getConfigData().getOfflineServerMotd()));
            eping.setResponse(pingResponse);
        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}
