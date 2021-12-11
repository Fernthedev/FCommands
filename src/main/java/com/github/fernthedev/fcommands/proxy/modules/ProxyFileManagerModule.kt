package com.github.fernthedev.fcommands.proxy.modules

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.proxy.ProxyFileManager
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.proxy.data.IPDeleteValues
import com.github.fernthedev.fcommands.proxy.data.IPSaveValues
import com.github.fernthedev.fcommands.proxy.data.SeenValues
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.TypeLiteral

inline fun <reified T> typeLiteral() = object : TypeLiteral<T>() { }

class ProxyFileManagerModule : AbstractModule() {

    @Provides
    fun provideConfig(proxyFileManager: ProxyFileManager): Config<ConfigValues> {
        return proxyFileManager.configManager
    }

    @Provides
    fun provideIpConfig(proxyFileManager: ProxyFileManager): Config<IPSaveValues> {
        return proxyFileManager.ipConfig
    }

    @Provides
    fun provideIpDeleteConfig(proxyFileManager: ProxyFileManager): Config<IPDeleteValues> {
        return proxyFileManager.deleteIPConfig
    }

    @Provides
    fun provideSeenConfig(proxyFileManager: ProxyFileManager): Config<SeenValues> {
        return proxyFileManager.seenConfig
    }
}