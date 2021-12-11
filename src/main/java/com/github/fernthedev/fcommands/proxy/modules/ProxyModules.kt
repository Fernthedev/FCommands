package com.github.fernthedev.fcommands.proxy.modules

import com.google.inject.AbstractModule

class ProxyModules : AbstractModule() {
    /** Configures a [Binder] via the exposed methods.  */
    override fun configure() {
        install(ProxyFileManagerModule())
    }
}