package com.github.fernthedev.fcommands.spigot.misc;

import com.github.fernthedev.fcommands.spigot.FernCommands;
import com.github.fernthedev.fcommands.spigot.FilesManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class RandomKick {

    public void registerListener() {
        boolean randomKick = FilesManager.getInstance().getValue("RandomKick", false);

        String randomKickMessageCountdown = FilesManager.getInstance().getValue("RandomKickMessageCountdown", "&c&lRemoving trash in %count%");
        String randomKickMessage = FilesManager.getInstance().getValue("RandomKickMessage", "&a&lRemoved %player% which is trash");
        long randomKickCountTimeSeconds = FilesManager.getInstance().getValue("RandomKickCountTimeSeconds", 5 * 60);


        Bukkit.getScheduler().scheduleSyncRepeatingTask(FernCommands.getInstance(), () -> {
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
