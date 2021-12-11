package com.github.fernthedev.fcommands.bungee.modules

import com.github.fernthedev.fcommands.proxy.modules.ProxyModules
import com.google.inject.AbstractModule

class BungeeModules : AbstractModule() {
    /** Configures a [Binder] via the exposed methods.  */
    override fun configure() {
        install(ProxyModules())
        install(BungeeEventsModule())
    }
}