package com.github.fernthedev.fcommands.velocity.modules

import com.github.fernthedev.fcommands.proxy.modules.ProxyModules
import com.google.inject.AbstractModule

class VelocityModules : AbstractModule() {
    /** Configures a [Binder] via the exposed methods.  */
    override fun configure() {
        install(ProxyModules())
        install(VelocityEventsModule())
    }
}