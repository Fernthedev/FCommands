package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.fcommands.proxy.commands.FernMain
import com.github.fernthedev.fcommands.proxy.commands.FernNick
import com.github.fernthedev.fcommands.proxy.commands.GetPlaceholderCommand
import com.github.fernthedev.fcommands.proxy.commands.Seen
import com.github.fernthedev.fcommands.proxy.commands.ip.MainIP
import com.github.fernthedev.fcommands.proxy.commands.ip.ShowAlts
import com.github.fernthedev.fcommands.universal.DBManager
import com.github.fernthedev.fcommands.universal.PlatformAllRegistration
import com.github.fernthedev.fcommands.universal.PluginPreferenceManager
import com.github.fernthedev.fcommands.universal.UniversalMysql
import com.github.fernthedev.fcommands.universal.commands.NameHistory
import com.github.fernthedev.fernapi.universal.Universal

object ProxyRegistration {
    fun proxyInit() {
        val injector = PlatformAllRegistration.injector

        ServerMaintenance.setupTask()
        Universal.getCommandHandler().registerCommand(injector.getInstance(FernMain::class.java))
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
        if (FileManager.configValues.allowIPShow) {
            Universal.getCommandHandler().registerCommand(MainIP())
        }
        if (FileManager.configValues.showAltsCommand) {
            Universal.getCommandHandler().registerCommand(ShowAlts())
        }
        if (FileManager.configValues.allowIPDelete) {
            MainIP.loadTasks()
        }
        if (FileManager.configValues.allowNameHistory) {
//            getProxy().getPluginManager().registerCommand(this, new NameHistory());
            Universal.getCommandHandler().registerCommand(NameHistory())
        }

        //SEEN COMMAND
        if (FileManager.configValues.allowSeenCommand) {
            Universal.getCommandHandler().registerCommand(Seen())
        }
        val configValues = FileManager.configManager.configData
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
        if (FileManager.configValues.globalNicks && configValues.databaseConnect) {
            Universal.getCommandHandler().registerCommand(FernNick())
        }
        Universal.getCommandHandler().registerCommand(GetPlaceholderCommand())
    }
}