package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.data.ServerData;
import com.github.fernthedev.fernapi.universal.Universal;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.server.ServerPing;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerMaintenanceMOTD {

    private Map<String, Boolean> serverMap = new HashMap<>();

    public void setupTask() {
        Universal.getScheduler().runSchedule(() -> {
            try {
                Universal.debug("Pinging the servers");

                for (ServerData serverData : FileManager.getConfigValues().getServerChecks()) {


                    Universal.getScheduler().runAsync(() -> {
                        serverData.ping();
                        Universal.debug("Pinging the server info Name:" + serverData.getName() + " status: " + serverData.isOnline() + " " + serverData);
                        serverMap.put(serverData.getName(), serverData.isOnline());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, getAverageTimeMS(), TimeUnit.MILLISECONDS);
    }

    private boolean isOnline() {
        for (boolean serverData : serverMap.values()) {
            if (!serverData) return false;
        }

        return true;
    }


    private int getAverageTimeMS() {
        int totalMS = 0;
        int amount = FileManager.getConfigValues().getServerChecks().size();

        for (ServerData serverData : FileManager.getConfigValues().getServerChecks()) {
            totalMS += serverData.getTimeoutMS();
        }
        return totalMS / amount;
    }

    @Subscribe
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (FileManager.getConfigValues().isUseMotd() && !isOnline()) {
            ServerPing pingResponse = eping.getPing();

            pingResponse.asBuilder().description(LegacyComponentSerializer.legacyLinking().deserialize(FileManager.getConfigValues().getOfflineServerMotd()));
            eping.setPing(pingResponse);

        }
    }
}




