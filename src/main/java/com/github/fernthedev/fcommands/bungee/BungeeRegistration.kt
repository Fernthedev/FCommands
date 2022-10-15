package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.bungee.modules.BungeeModules
import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.google.inject.Injector

object BungeeRegistration {

    @JvmStatic
    fun bungeeInit(injector: Injector): Injector {
        return ProxyRegistration.proxyInit(buildInjector(injector))
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector(injector: Injector): Injector {
        return injector.createChildInjector(
            BungeeModules()
        )
    }

}