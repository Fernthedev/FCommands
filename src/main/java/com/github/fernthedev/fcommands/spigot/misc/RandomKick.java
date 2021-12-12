package com.github.fernthedev.fcommands.spigot.misc;

import com.github.fernthedev.fcommands.spigot.SpigotFileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import javax.inject.Inject;

public class RandomKick {

    @Inject
    private SpigotFileManager spigotFileManager;

    @Inject
    private Plugin plugin;

    public void registerListener() {
        boolean randomKick = spigotFileManager.getValue("RandomKick", false);

        String randomKickMessageCountdown = spigotFileManager.getValue("RandomKickMessageCountdown", "&c&lRemoving trash in %count%");
        String randomKickMessage = spigotFileManager.getValue("RandomKickMessage", "&a&lRemoved %player% which is trash");
        long randomKickCountTimeSeconds = spigotFileManager.getValue("RandomKickCountTimeSeconds", 5 * 60);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            String countDown = ChatColor.translateAlternateColorCodes('&', randomKickMessageCountdown);

            for (int i = 10; i > 0; i--) {
                Bukkit.broadcastMessage(countDown.replace("%count%", i + ""));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }, randomKickCountTimeSeconds * 20, randomKickCountTimeSeconds * 20);
    }

}
