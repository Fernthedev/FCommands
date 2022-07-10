package com.github.fernthedev.fcommands.bungee.modules

import com.github.fernthedev.fcommands.bungee.BungeeEvents
import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fcommands.toType
import com.github.fernthedev.fcommands.universal.BaseEvents
import com.google.inject.AbstractModule

class BungeeEventsModule : AbstractModule() {

    override fun configure() {
        bind(BungeeEvents::class.java)
        bind(ProxyEvents::class.java).toType(BungeeEvents::class.java)
        bind(BaseEvents::class.java).toType(BungeeEvents::class.java)
    }
}