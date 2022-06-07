package com.github.fernthedev.fcommands.spigot.modules

import com.github.fernthedev.fcommands.spigot.FernCommands
import com.google.inject.AbstractModule
import com.google.inject.util.Providers
import org.bukkit.Server
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class SpigotModules(
    val plugin: FernCommands
) : AbstractModule() {
    /** Configures a [Binder] via the exposed methods.  */
    override fun configure() {
        // Use providers so the instance itself
        // doesn't have injectMembers called on it
        bind(Plugin::class.java)
            .toProvider(Providers.of(plugin))

        bind(FernCommands::class.java)
            .toProvider(Providers.of(plugin))

        bind(Server::class.java)
            .toProvider(Providers.of(plugin.server))

        bind(FileConfiguration::class.java)
            .toProvider(Providers.of(plugin.config))

        install(SpigotFileManagerModules())
    }
}