package com.github.fernthedev.fcommands.velocity;

import com.github.fernthedev.fcommands.proxy.FileManager;
import com.github.fernthedev.fcommands.proxy.commands.Seen;
import com.github.fernthedev.fernapi.universal.Universal;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;

public class Events {

    @Subscribe
    public void onLeave(DisconnectEvent e) {
        if(FileManager.getConfigManager().getConfigData().isAllowSeenCommand()) Seen.
                onLeave(Universal.getMethods().convertPlayerObjectToFPlayer(e.getPlayer()));
    }

}
