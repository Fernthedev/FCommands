package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VelocityEvents @Inject constructor(private val methodHandler: MethodInterface<*, *>) : ProxyEvents() {

    @Subscribe
    fun onLeave(e: DisconnectEvent) {
        onLeave(methodHandler.convertPlayerObjectToFPlayer(e.player))
    }

}