package com.github.fernthedev.fcommands.proxy;

import com.github.fernthedev.config.common.Config;
import com.github.fernthedev.fcommands.proxy.data.ConfigValues;
import com.github.fernthedev.fcommands.proxy.data.ServerData;
import com.github.fernthedev.fernapi.universal.APIHandler;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class ServerMaintenance {

    private final Map<String, Boolean> serverMap = new HashMap<>();

    @Inject
    private Config<ConfigValues> config;

    @Inject
    private IScheduler<?, ?> scheduler;

    public void setupTask() {
        scheduler.runSchedule(() -> {
            try {
                APIHandler.debug(() ->"Pinging the servers");

                for (ServerData serverData : config.getConfigData().getServerChecks()) {
                    scheduler.runAsync(() -> {
                        serverData.ping();
                        APIHandler.debug(() ->"Pinging the server info Name:" + serverData.getName() + " status: " + serverData.getOnline() + " " + serverData);
                        serverMap.put(serverData.getName(), serverData.getOnline());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, getAverageTimeMS() + 1000, TimeUnit.MILLISECONDS);
    }

    public boolean isOnline() {
        return serverMap.values().stream().anyMatch(serverOn -> serverOn);
    }


    private int getAverageTimeMS() {
        int totalMS = 0;
        int amount = config.getConfigData().getServerChecks().size();

        for (ServerData serverData : config.getConfigData().getServerChecks()) {
            totalMS += serverData.getTimeoutMS();
        }
        return totalMS / amount;
    }
}




