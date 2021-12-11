package com.github.fernthedev.fcommands.bungee

import com.google.inject.Injector
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.api.plugin.PluginManager

inline fun <reified T: Listener> PluginManager.registerListenerInjected(plugin: Plugin, injector: Injector) {
    registerListener(plugin, injector.getInstance(T::class.java))
}