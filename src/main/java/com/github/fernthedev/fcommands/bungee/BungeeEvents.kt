package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BungeeEvents @Inject constructor(
    private val methodInterface: MethodInterface<*, *>
): ProxyEvents(), Listener {



    @EventHandler
    fun onLeave(e: PlayerDisconnectEvent) {
        onLeave(methodInterface.convertPlayerObjectToFPlayer(e.player))
    }
}