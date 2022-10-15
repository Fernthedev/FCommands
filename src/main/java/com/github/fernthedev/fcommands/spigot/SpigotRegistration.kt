package com.github.fernthedev.fcommands.spigot

import com.github.fernthedev.fcommands.spigot.modules.SpigotModules
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.google.inject.Injector

object SpigotRegistration {

    @JvmStatic
    fun spigotInit(plugin: FernCommands, injector: Injector): Injector {
        val injector = buildInjector(plugin, injector)

        PlatformAllRegistration.commonInit(injector)
        return injector
    }

    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    private fun buildInjector(plugin: FernCommands, injector: Injector): Injector {
        return injector.createChildInjector(
            SpigotModules(plugin)
        )
    }

}