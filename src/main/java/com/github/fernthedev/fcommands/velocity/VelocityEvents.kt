package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fernapi.universal.Universal
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import javax.inject.Singleton

@Singleton
class VelocityEvents : ProxyEvents() {

    @Subscribe
    fun onLeave(e: DisconnectEvent) {
        onLeave(Universal.getMethods().convertPlayerObjectToFPlayer(e.player))
    }

}