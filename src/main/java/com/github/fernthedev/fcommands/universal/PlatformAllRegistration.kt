package com.github.fernthedev.fcommands.universal

import com.github.fernthedev.fcommands.universal.commands.DebugCommand
import com.github.fernthedev.fcommands.universal.commands.UFernPing
import com.github.fernthedev.fernapi.universal.APIHandler
import com.google.inject.Injector

object PlatformAllRegistration {
//    private var internalInjector: Injector
//
//    init {
//        internalInjector = Guice.createInjector()
//    }


    /**
     * Injectors will be built from this order:
     * First Top -> last Down
     * Common
     *  Proxy
     *    Bungee
     *    Velocity
     *  Spigot
     *  Sponge
     */
    @JvmStatic
    lateinit var injector: Injector
//        get() {
//            return internalInjector
//        }
//        set(value) {
//            check((value.parent === internalInjector)) { "New injector must be child of current injector" }
//            internalInjector = injector
//        }

    @JvmStatic
    fun commonInit(injector: Injector): Injector {
        PlatformAllRegistration.injector = injector
        val apiHandler = injector.getInstance(APIHandler::class.java)
        apiHandler.commandManager.enableUnstableAPI("help")
        apiHandler.commandManager.registerCommand(UFernPing())
        apiHandler.commandManager.registerCommand(DebugCommand())

        if (UniversalMysql.getDatabaseManager() != null) {
            apiHandler.messageHandler.registerMessageHandler(injector.getInstance(NickNetworkManager::class.java))
            apiHandler.logger.info("Registered fern nicks velocity channels.")
        }
        return PlatformAllRegistration.injector
    }
}