package com.github.fernthedev.fcommands.universal

import com.github.fernthedev.fcommands.proxy.ProxyRegistration
import com.github.fernthedev.fcommands.universal.commands.DebugCommand
import com.github.fernthedev.fcommands.universal.commands.UFernPing
import com.github.fernthedev.fernapi.universal.Universal
import com.google.inject.Guice
import com.google.inject.Injector

object PlatformAllRegistration {
    private var internalInjector: Injector

    init {
        internalInjector = Guice.createInjector()
    }

    @JvmStatic
    var injector: Injector
        get() {
            return internalInjector
        }
        set(value) {
            check(!(value.parent !== internalInjector)) { "New injector must be child of current injector" }
            internalInjector = injector
        }

    @JvmStatic
    fun commonInit() {
        Universal.getCommandHandler().enableUnstableAPI("help")
        Universal.getCommandHandler().registerCommand(UFernPing())
        Universal.getCommandHandler().registerCommand(DebugCommand())
        ProxyRegistration.proxyInit()

        if (UniversalMysql.getDatabaseManager() != null) {
            Universal.getMessageHandler().registerMessageHandler(NickNetworkManager())
            Universal.getLogger().info("Registered fern nicks velocity channels.")
        }
    }
}