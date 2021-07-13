package com.github.fernthedev.fcommands.bungee;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.commands.Seen;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {

    @EventHandler
    public void onLeave(PlayerDisconnectEvent e) {
        if(FileManager.getConfigManager().getConfigData().getAllowSeenCommand()) Seen.
                onLeave(Universal.getMethods().convertPlayerObjectToFPlayer(e.getPlayer()));
    }

}
