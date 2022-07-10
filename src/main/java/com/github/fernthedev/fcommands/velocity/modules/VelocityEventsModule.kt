package com.github.fernthedev.fcommands.velocity.modules

import com.github.fernthedev.fcommands.proxy.ProxyEvents
import com.github.fernthedev.fcommands.toType
import com.github.fernthedev.fcommands.universal.BaseEvents
import com.github.fernthedev.fcommands.velocity.VelocityEvents
import com.google.inject.AbstractModule

class VelocityEventsModule : AbstractModule() {

    override fun configure() {
        bind(VelocityEvents::class.java)
        bind(ProxyEvents::class.java).toType(VelocityEvents::class.java)
        bind(BaseEvents::class.java).toType(VelocityEvents::class.java)
    }
}