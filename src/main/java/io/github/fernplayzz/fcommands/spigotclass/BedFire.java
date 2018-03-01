package io.github.fernplayzz.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class BedFire extends JavaPlugin implements Listener {
    public Player players = (Player) Bukkit.getServer().getOnlinePlayers();
    for(Player p : Bukkit.getOnlinePlayers()){
        if (players.isSleeping()) {
            players.setFireTicks(200);
            players.sendMessage(ChatColor.GOLD + "YOU'RE NOW ON FIRE THANKS TO A SHAODW! HAHAHAHAH");
        }
    }
}
