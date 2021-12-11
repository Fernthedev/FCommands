package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.ServerMaintenance;
import com.github.fernthedev.fcommands.proxy.WhichFile;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.modules.ProxyFile;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import javax.inject.Inject;

public class VelocityServerMotdPing {

    @Inject
    @ProxyFile(WhichFile.CONFIG)
    private Config<ConfigValues> config;

    @Inject
    private ServerMaintenance serverMaintenance;

    @Subscribe
    public void maintenanceMOTD(ProxyPingEvent eping) {
        ConfigValues configData = config.getConfigData();
        if (configData.getUseMotd() && !serverMaintenance.isOnline()) {
            ServerPing pingResponse = eping.getPing();

            pingResponse.asBuilder().description(LegacyComponentSerializer.legacyAmpersand().deserialize(configData.getOfflineServerMotd()));
            eping.setPing(pingResponse);
        }
    }
}




