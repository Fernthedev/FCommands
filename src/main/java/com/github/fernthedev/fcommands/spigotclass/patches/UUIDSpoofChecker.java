package com.github.fernthedev.fcommands.spigotclass.patches;

import com.github.fernthedev.fcommands.spigotclass.FernCommands;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UUIDSpoofChecker implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        FernCommands.getInstance().getLogger().info("Checking for uuid spoof");
        if(!e.getPlayer().getUniqueId().toString().equals(UUIDFetcher.getUUID(e.getPlayer().getName()))) {
            FernCommands.getInstance().getLogger().info("UUID Spoof!");
            e.getPlayer().kickPlayer("UUID Spoof?");
        }
    }

}
