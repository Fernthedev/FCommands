package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.bungee.modules.BungeeModules
import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.google.inject.Guice

object BungeeRegistration {

    @JvmStatic
    fun bungeeInit() {
        buildInjector()

        ProxyRegistration.proxyInit()
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector() {
        PlatformAllRegistration.injector = Guice.createInjector(
            BungeeModules()
        )
    }

}