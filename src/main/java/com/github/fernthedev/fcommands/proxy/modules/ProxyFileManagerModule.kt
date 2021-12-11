package com.github.fernthedev.fcommands.proxy.modules

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.proxy.ProxyFileManager
import com.github.fernthedev.fcommands.proxy.WhichFile
import com.github.fernthedev.fcommands.proxy.WhichFile.*
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.TypeLiteral
import javax.inject.Qualifier

inline fun <reified T> typeLiteral() = object : TypeLiteral<T>() { }

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProxyFile(val value: WhichFile)

class ProxyFileManagerModule : AbstractModule() {

    override fun configure() {
        // TODO: Interface?
        bind(ProxyFileManager::class.java)
            .to(ProxyFileManager::class.java)

//        bind(typeLiteral<Config<*>>())
//            .annotatedWith(ProxyFile::class.java)
//            .toProvider(provideProxyConfig)
    }

    @Provides
    fun provideProxyConfig(proxyFileManager: ProxyFileManager, whichFile: WhichFile): Config<*> {
        return when (whichFile) {
            IP -> proxyFileManager.ipConfig
            CONFIG -> proxyFileManager.configManager
            SEEN -> proxyFileManager.seenConfig
            DELETEIP -> proxyFileManager.deleteIPConfig
            else -> {
                throw IllegalArgumentException("$whichFile is not supported!")
            }
        }
    }
}