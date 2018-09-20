package io.github.fernthedev.fcommands.spigotclass.methods;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
import io.github.fernthedev.fcommands.Universal.Universal;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static io.github.fernthedev.fcommands.Universal.UUIDFetcher.*;

public class UUIDFetcherSpigot {

    private BukkitTask requestBukkitRunnable;

    private static BukkitTask banBukkitRunnable;



    public void runTimerRequest() {
        print("Server is bukkit");
        this.requestBukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                UUIDFetcher.setRequests(0);
                playerNameCache.clear();
                playerUUIDCache.clear();
                playerHistoryCache.clear();
                print("Refreshed uuid cache.");
            }
        }.runTaskLater(Universal.getMethods().getSpigotInstance(),
                TimeUnit.MINUTES.toSeconds(10) * 20);
    }

    public void runHourTask() {
        banBukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!hourRan && didHourCheck) {
                    hourRan = true;
                    print("Hour is finished, continuing uuid checking");
                    runTimerRequest();
                    stopHourTask();
                } else if (!didHourCheck) didHourCheck = true;
            }
        }.runTaskLater(Universal.getMethods().getSpigotInstance(),
                TimeUnit.HOURS.toSeconds(1) *20);
    }

    public void stopTimerRequest() {
        if(requestBukkitRunnable != null) {
            Universal.getMethods().getSpigotInstance().getServer().getScheduler().cancelTask(requestBukkitRunnable.getTaskId());
        }
    }

    public void stopHourTask() {
        Universal.getMethods().getSpigotInstance().getServer().getScheduler().cancelTask(banBukkitRunnable.getTaskId());
    }



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[UUIDFetcher] " + log);
    }

}
