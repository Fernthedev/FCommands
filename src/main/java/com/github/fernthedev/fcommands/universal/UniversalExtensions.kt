package com.github.fernthedev.fcommands.universal

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandManager
import com.google.inject.Injector

inline fun <reified T: BaseCommand> CommandManager<*,*,*,*,*,*>.registerCommandInjected(injector: Injector): T {
    val command = injector.getInstance(T::class.java)
    registerCommand(command)
    return command
}