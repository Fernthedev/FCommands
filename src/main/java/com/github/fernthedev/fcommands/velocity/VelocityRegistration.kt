package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.github.fernthedev.fcommands.velocity.modules.VelocityModules
import com.google.inject.Guice

object VelocityRegistration {

    @JvmStatic
    fun velocityInit() {
        buildInjector()

        ProxyRegistration.proxyInit()
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector() {
        PlatformAllRegistration.injector = Guice.createInjector(
            VelocityModules()
        )
    }

}