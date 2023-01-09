package com.github.fernthedev.fcommands.spigot.modules

import com.github.fernthedev.fcommands.spigot.FernCommands
import com.google.inject.AbstractModule
import com.google.inject.util.Providers
import org.bukkit.configuration.file.FileConfiguration

class SpigotModules(
    val plugin: FernCommands
) : AbstractModule() {
    /** Configures a [Binder] via the exposed methods.  */
    override fun configure() {
        // Use providers so the instance itself
        // doesn't have injectMembers called on it
        bind(FernCommands::class.java)
            .toProvider(Providers.of(plugin))


        bind(FileConfiguration::class.java)
            .toProvider(Providers.of(plugin.config))

        install(SpigotFileManagerModules())
    }
}