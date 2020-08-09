package com.github.fernthedev.fcommands.bungeeclass;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ServerMaintenanceMOTD implements Listener {

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

    @EventHandler
    public void maintenanceMOTD(ProxyPingEvent eping) {
        if (FileManager.getConfigValues().isUseMotd() && !isOnline()) {
//                    try {
//                        FernCommands.getFileManager().loadFiles("config", true);
//                    } catch (IOException e) {
//                        FernCommands.getInstance().getLogger().warning("Unable to load config");
//                    }
//                    ServerPing pingResponse = eping.getResponse();
//
//                    String motd = LegacyFileManager.getConfigValues().getMotd();
//                    if (motd == null) {
//                        ProxyServer.getInstance().getLogger().warning("Unable to find MOTD");
//                    } else {
//
//                        if (motd.isEmpty()) {
//                            FernCommands.getInstance().getLogger().warning("Motd is null oh no!");
//                            return;
//                        }
//
//                        motd = motd.replace("\\n", "\n");
//
//                        pingResponse.setDescriptionComponent(message(motd));
//                        eping.setResponse(pingResponse);
//                    }
            ServerPing pingResponse = eping.getResponse();


            pingResponse.setDescriptionComponent(message(FileManager.getConfigValues().getOfflineServerMotd()));
            eping.setResponse(pingResponse);


        }
    }

    public BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}




