package com.github.fernthedev.fcommands.velocity

import com.google.inject.Injector
import com.velocitypowered.api.event.EventManager

inline fun <reified T> EventManager.registerInjected(plugin: Any, injector: Injector) {
    register(plugin, injector.getInstance(T::class.java))
}