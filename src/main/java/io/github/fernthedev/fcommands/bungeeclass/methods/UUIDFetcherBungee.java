package io.github.fernthedev.fcommands.bungeeclass.methods;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
import io.github.fernthedev.fcommands.Universal.Universal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;


import java.util.concurrent.TimeUnit;

import static io.github.fernthedev.fcommands.Universal.UUIDFetcher.*;

public class UUIDFetcherBungee {

    private static ScheduledTask requestTask;

    private static ScheduledTask banHourTask;



    public void runTimerRequest() {
        print("Server is bungee");
        requestTask = ProxyServer.getInstance().getScheduler().schedule(Universal.getMethods().getBungeeInstance(), () -> {
            UUIDFetcher.setRequests(0);
            playerNameCache.clear();
            playerUUIDCache.clear();
            playerHistoryCache.clear();
        }, 1, 10, TimeUnit.MINUTES);
    }

    public void runHourTask() {

        banHourTask = ProxyServer.getInstance().getScheduler().schedule(Universal.getMethods().getBungeeInstance(), () -> {
            if (!hourRan && didHourCheck) {
                hourRan = true;
                addRequestTimer();
                stopHourTask();
            } else if (!didHourCheck) didHourCheck = true;
        }, 1, 1, TimeUnit.HOURS);

    }

    public void stopTimerRequest() {
        ProxyServer.getInstance().getScheduler().cancel(banHourTask);
    }

    public void stopHourTask() {
        ProxyServer.getInstance().getScheduler().cancel(requestTask);

    }



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[UUIDFetcher] " + log);
    }

}
