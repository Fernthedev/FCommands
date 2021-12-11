package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fernapi.universal.Universal
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import javax.inject.Singleton

@Singleton
class BungeeEvents : ProxyEvents(), Listener {

    @EventHandler
    fun onLeave(e: PlayerDisconnectEvent) {
        onLeave(Universal.getMethods().convertPlayerObjectToFPlayer(e.player))
    }
}