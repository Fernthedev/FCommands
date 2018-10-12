package io.github.fernthedev.fcommands.bungeeclass.patches;

import io.github.fernthedev.fcommands.Universal.UUIDFetcher;
import io.github.fernthedev.fcommands.bungeeclass.FernCommands;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;



public class UUIDSpoofChecker implements Listener {

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        FernCommands.getInstance().getLogger().info("Checking for UUID Spoof!");
        if(!e.getPlayer().getUniqueId().toString().equals(UUIDFetcher.getUUID(e.getPlayer().getName()))) {
            FernCommands.getInstance().getLogger().info("UUID Spoof!");
            e.getPlayer().disconnect(FernCommands.getInstance().message("UUID Spoof?"));
        }
    }

}
