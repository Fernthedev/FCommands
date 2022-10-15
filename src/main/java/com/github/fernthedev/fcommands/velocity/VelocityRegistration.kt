package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.github.fernthedev.fcommands.velocity.modules.VelocityModules
import com.google.inject.Injector

object VelocityRegistration {

    @JvmStatic
    fun velocityInit(injector: Injector): Injector {
        return ProxyRegistration.proxyInit(buildInjector(injector))
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector(injector: Injector): Injector {
        return injector.createChildInjector(
            VelocityModules()
        )
    }

}