package com.github.fernthedev.fcommands.spigot.modules

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.spigot.NewSpigotConfig
import com.github.fernthedev.fcommands.spigot.SpigotFileManager
import com.google.inject.AbstractModule
import com.google.inject.Provides

class SpigotFileManagerModules : AbstractModule() {

    @Provides
    fun provideConfig(spigotFileManager: SpigotFileManager): Config<NewSpigotConfig> {
        return spigotFileManager.newSpigotConfigConfig
    }
}