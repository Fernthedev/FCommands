package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.fcommands.proxy.commands.FernMain
import com.github.fernthedev.fcommands.proxy.commands.FernNick
import com.github.fernthedev.fcommands.proxy.commands.Seen
import com.github.fernthedev.fcommands.proxy.commands.ip.MainIP
import com.github.fernthedev.fcommands.proxy.commands.ip.ShowAlts
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.universal.*
import com.github.fernthedev.fernapi.universal.APIHandler
import com.google.inject.Injector

object ProxyRegistration {
    fun proxyInit(injector: Injector): Injector {
        PlatformAllRegistration.commonInit(injector)

        injector.getInstance(ServerMaintenance::class.java).setupTask()
        val apiHandler = injector.getInstance(APIHandler::class.java)
        apiHandler.commandManager.registerCommandInjected<FernMain>(injector)
        if (apiHandler.methods.serverType.isProxy) {
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
            apiHandler.commandManager.registerCommand(mainIp)
        }
        if (configValues.showAltsCommand) {
            apiHandler.commandManager.registerCommandInjected<ShowAlts>(injector)
        }
        if (configValues.allowIPDelete) {
            mainIp.loadTasks()
        }




        //SEEN COMMAND
        if (configValues.allowSeenCommand) {
            apiHandler.commandManager.registerCommandInjected<Seen>(injector)
        }

        val databaseAuthInfo = configValues.databaseAuthInfo
        if (configValues.databaseConnect) {
            apiHandler.logger
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
            apiHandler.commandManager.registerCommandInjected<FernNick>(injector)
        }

        return injector
    }
}