package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.fcommands.proxy.commands.FernMain
import com.github.fernthedev.fcommands.proxy.commands.FernNick
import com.github.fernthedev.fcommands.proxy.commands.GetPlaceholderCommand
import com.github.fernthedev.fcommands.proxy.commands.Seen
import com.github.fernthedev.fcommands.proxy.commands.ip.MainIP
import com.github.fernthedev.fcommands.proxy.commands.ip.ShowAlts
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.proxy.modules.ProxyFileManagerModule
import com.github.fernthedev.fcommands.universal.*
import com.github.fernthedev.fcommands.universal.commands.NameHistory
import com.github.fernthedev.fernapi.universal.Universal

object ProxyRegistration {
    /**
     * Order:
     * @see PlatformAllRegistration.injector
     */
    fun buildInjector() {
        PlatformAllRegistration.injector = PlatformAllRegistration.injector.createChildInjector(
            ProxyFileManagerModule()
        )
    }

    fun proxyInit() {
        PlatformAllRegistration.commonInit()
        val injector = PlatformAllRegistration.injector

        injector.getInstance(ServerMaintenance::class.java).setupTask()

        Universal.getCommandHandler().registerCommandInjected<FernMain>(injector)
        if (Universal.getMethods().serverType.isProxy) {
            try {
                // Use reflection to avoid classpath issues with Spigot
                val aClass = Class.forName("com.github.fernthedev.preferences.api.PreferenceManager")
                val preferenceClass = Class.forName("com.github.fernthedev.preferences.api.PluginPreference")
                aClass.getMethod("registerPreference", preferenceClass).invoke(null, PluginPreferenceManager())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val proxyFileManager = injector.getInstance(ProxyFileManager::class.java)
        val configValues: ConfigValues = proxyFileManager.configManager.configData

        val mainIp = injector.getInstance(MainIP::class.java)

        if (configValues.allowIPShow) {
            Universal.getCommandHandler().registerCommand(mainIp)
        }
        if (configValues.showAltsCommand) {
            Universal.getCommandHandler().registerCommandInjected<ShowAlts>(injector)
        }
        if (configValues.allowIPDelete) {
            mainIp.loadTasks()
        }
        if (configValues.allowNameHistory) {
//            getProxy().getPluginManager().registerCommand(this, new NameHistory());
            Universal.getCommandHandler().registerCommand(NameHistory())
        }



        //SEEN COMMAND
        if (configValues.allowSeenCommand) {
            Universal.getCommandHandler().registerCommandInjected<Seen>(injector)
        }

        val databaseAuthInfo = configValues.databaseAuthInfo
        if (configValues.databaseConnect) {
            Universal.getLogger()
                .info("Using database info: database: ${databaseAuthInfo.database} host: ${databaseAuthInfo.urlHost} port: ${databaseAuthInfo.port} user: ${databaseAuthInfo.username}")
            val databaseManager = DBManager(
                databaseAuthInfo.username,
                databaseAuthInfo.password,
                databaseAuthInfo.port,
                databaseAuthInfo.urlHost,
                databaseAuthInfo.database
            )
            UniversalMysql.setDatabaseManager(databaseManager)
        }
        if (configValues.globalNicks && configValues.databaseConnect) {
            Universal.getCommandHandler().registerCommandInjected<FernNick>(injector)
        }
        Universal.getCommandHandler().registerCommand(GetPlaceholderCommand())
    }
}