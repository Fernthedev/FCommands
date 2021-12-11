package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.bungee.modules.BungeeEventsModule
import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration

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
        ProxyRegistration.buildInjector()
        PlatformAllRegistration.injector = PlatformAllRegistration.injector.createChildInjector(
            BungeeEventsModule()
        )
    }

}