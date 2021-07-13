package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.ServerMaintenance;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class VelocityServerMotdPing {

    @Subscribe
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (FileManager.getConfigValues().getUseMotd() && !ServerMaintenance.isOnline()) {
            ServerPing pingResponse = eping.getPing();

            pingResponse.asBuilder().description(LegacyComponentSerializer.legacyAmpersand().deserialize(FileManager.getConfigValues().getOfflineServerMotd()));
            eping.setPing(pingResponse);

        }
    }
}




