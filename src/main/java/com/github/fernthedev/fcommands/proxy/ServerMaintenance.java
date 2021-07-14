package com.github.fernthedev.fcommands.proxy;

import com.github.fernthedev.fcommands.proxy.data.ServerData;
import com.github.fernthedev.fernapi.universal.Universal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerMaintenance {

    private static Map<String, Boolean> serverMap = new HashMap<>();

    public static void setupTask() {
        Universal.getScheduler().runSchedule(() -> {
            try {
                Universal.debug(() ->"Pinging the servers");

                for (ServerData serverData : FileManager.getConfigValues().getServerChecks()) {
                    Universal.getScheduler().runAsync(() -> {
                        serverData.ping();
                        Universal.debug(() ->"Pinging the server info Name:" + serverData.getName() + " status: " + serverData.getOnline() + " " + serverData);
                        serverMap.put(serverData.getName(), serverData.getOnline());
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, getAverageTimeMS() + 1000, TimeUnit.MILLISECONDS);
    }

    public static boolean isOnline() {
        return serverMap.values().stream().anyMatch(serverOn -> serverOn);
    }


    private static int getAverageTimeMS() {
        int totalMS = 0;
        int amount = FileManager.getConfigValues().getServerChecks().size();

        for (ServerData serverData : FileManager.getConfigValues().getServerChecks()) {
            totalMS += serverData.getTimeoutMS();
        }
        return totalMS / amount;
    }
}




