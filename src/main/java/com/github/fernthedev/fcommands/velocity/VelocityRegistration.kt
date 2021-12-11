package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.github.fernthedev.fcommands.velocity.modules.VelocityEventsModule

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
        ProxyRegistration.buildInjector()
        PlatformAllRegistration.injector = PlatformAllRegistration.injector.createChildInjector(
            VelocityEventsModule()
        )
    }

}