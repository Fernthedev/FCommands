package com.github.fernthedev.fcommands.velocity

import com.github.fernthedev.fcommands.proxy.ProxyFileManager
import com.github.fernthedev.fcommands.velocity.VelocityRegistration.velocityInit
import com.github.fernthedev.fcommands.velocity.commands.VelocityPluginList
import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor
import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import org.slf4j.Logger

@Plugin(
    id = "fern_commands",
    name = "FernCommands",
    version = "\${version}",
    description = "A suite of stuff for Fern server",
    authors = ["Fernthedev"],
    dependencies = [Dependency(id = "preference_manager")]
)
class FernCommands @Inject constructor(server: ProxyServer?, logger: Logger?, pluginContainer: PluginContainer?, injector: Injector) :
    FernVelocityAPI(server, logger, pluginContainer, injector) {
    @Subscribe
    override fun onProxyInitialization(event: ProxyInitializeEvent) {
        super.onProxyInitialization(event)
        getLogger().info(ChatColor.BLUE.toString() + "ENABLED FERNCOMMANDS FOR VELOCITY")
        val dataFolder = dataDirectory.toFile()
        if (!dataFolder.exists()) {
            val mkdir = dataFolder.mkdir()
            getLogger().info("$mkdir folder make")
        }

        injector = velocityInit(injector)

        server.commandManager.register("vplugins", VelocityPluginList(server))
        server.eventManager.registerInjected<VelocityEvents>(this, injector)
        server.eventManager.registerInjected<VelocityServerMotdPing>(this, injector)

        getLogger().info("Registered fern nicks")
    }

    override fun onProxyStop(event: ProxyShutdownEvent) {
        super.onProxyStop(event)
        getLogger().info(ChatColor.GREEN.toString() + "SAVING FILES")

        val proxyFileManager = injector.getInstance(
            ProxyFileManager::class.java
        )

        proxyFileManager.configSave(proxyFileManager.ipConfig)
        proxyFileManager.configSave(proxyFileManager.seenConfig)
        proxyFileManager.configSave(proxyFileManager.configManager)
        proxyFileManager.configSave(proxyFileManager.deleteIPConfig)

        getLogger().info(ChatColor.GREEN.toString() + "FILED SUCCESSFULLY SAVED")
        getLogger().info(ChatColor.GREEN.toString() + "DISABLED FERNCOMMANDS FOR BUNGEECORD")
    }
}