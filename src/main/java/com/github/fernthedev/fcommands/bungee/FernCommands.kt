package com.github.fernthedev.fcommands.bungee

import com.github.fernthedev.fcommands.bungee.BungeeRegistration.bungeeInit
import com.github.fernthedev.fcommands.bungee.commands.BungeePluginList
import com.github.fernthedev.fcommands.bungee.commands.ip.AltsBan
import com.github.fernthedev.fcommands.proxy.ProxyFileManager
import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI
import net.md_5.bungee.api.ChatColor

class FernCommands : FernBungeeAPI() {
    override fun onEnable() {
        super.onEnable()
        logger.info(ChatColor.BLUE.toString() + "ENABLED FERNCOMMANDS FOR BUNGEECORD")
        if (!dataFolder.exists()) {
            val mkdir = dataFolder.mkdir()
            logger.info("$mkdir folder make")
        }
        hookManager = HookManager()
        injector = bungeeInit(injector)

        proxy.pluginManager.registerCommand(this, BungeePluginList())
        proxy.pluginManager.registerListenerInjected<BungeeEvents>(this, injector)


        //ADVANCEDBAN HOOK
        if (hookManager.hasAdvancedBan()) {
            logger.info(ChatColor.GREEN.toString() + "FOUND ADVANCEDBAN! HOOKING IN API")
            proxy.pluginManager.registerListenerInjected<AltsBan>(this, injector)
        }


        logger.info("Registered fern nicks bungee channels.")
        proxy.pluginManager.registerListenerInjected<PunishMOTD>(this, injector)
        proxy.pluginManager.registerListenerInjected<BungeeServerMotdPing>(this, injector)
        logger.info("Registered fern nicks")
    }

    override fun onDisable() {
        logger.info(ChatColor.GREEN.toString() + "SAVING FILES")
        val proxyFileManager = injector.getInstance(
            ProxyFileManager::class.java
        )
        proxyFileManager.configSave(proxyFileManager.ipConfig)
        proxyFileManager.configSave(proxyFileManager.seenConfig)
        proxyFileManager.configSave(proxyFileManager.configManager)
        proxyFileManager.configSave(proxyFileManager.deleteIPConfig)
        logger.info(ChatColor.GREEN.toString() + "FILED SUCCESSFULLY SAVED")
        super.onDisable()
        logger.info(ChatColor.GREEN.toString() + "DISABLED FERNCOMMANDS FOR BUNGEECORD")
        HookManager.onDisable()
    }

    companion object {
        @JvmStatic
        lateinit var hookManager: HookManager
        private set
    }
}