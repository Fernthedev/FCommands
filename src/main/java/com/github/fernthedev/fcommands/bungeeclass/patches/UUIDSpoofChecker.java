package com.github.fernthedev.fcommands.bungeeclass.patches;

import com.github.fernthedev.fcommands.bungeeclass.FernCommands;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


@Deprecated
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
