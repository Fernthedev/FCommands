package io.github.fernthedev.fcommands.spigotclass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

@SuppressWarnings("unused")
public class BedFire implements Listener {
    //Player players = (Player) BUKKIT.getServer().getOnlinePlayers();
    @EventHandler
    public static void onPlayerBedEnter(PlayerBedEnterEvent event) {
        FernCommands loginfo = new FernCommands();

        Bukkit.getLogger().info("LOADED BEDFIRE EVENT");
        //for (Player players : BUKKIT.getServer().getOnlinePlayers()) {
          /*  if (players.isSleeping()) {
                players.setFireTicks(200);
                players.sendMessage(ChatColor.GOLD + "YOU'RE NOW ON FIRE THANKS TO A SHAODW! HAHAHAHAH");
            }
        }*/

        Player target = event.getPlayer();
        if (!(target.isInvulnerable())) {
            if (target.getGameMode() != GameMode.CREATIVE) {
                target.setFireTicks(200);
                target.sendMessage(ChatColor.GOLD + "A Shadow Burnt you to death. Maybe.");
                Bukkit.getLogger().info("Burnt inbed: " + target.getDisplayName());
            }
        }
    }
    public static void onEnable() {
        Bukkit.getLogger().info("ENABLED BEDFIRE");
    }
}
    /*@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(getServer().getPluginCommand(BedFire)) == true) {
            sender.sendMessage(ChatColor.GREEN + "Bedfire is enabled");
        }else{
            sender.sendMessage(ChatColor.RED + "Bedfire is not enabled");
        }
        return true;
    }*/


