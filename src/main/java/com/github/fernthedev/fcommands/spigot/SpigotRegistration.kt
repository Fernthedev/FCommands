package com.github.fernthedev.fcommands.spigot

import com.github.fernthedev.fcommands.spigot.modules.SpigotModules
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.google.inject.Guice

object SpigotRegistration {

    @JvmStatic
    fun spigotInit(plugin: FernCommands) {
        buildInjector(plugin)

        PlatformAllRegistration.commonInit()
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector(plugin: FernCommands) {
        PlatformAllRegistration.injector = Guice.createInjector(
            SpigotModules(plugin)
        )
    }

}