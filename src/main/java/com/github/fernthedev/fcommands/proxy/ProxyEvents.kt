package com.github.fernthedev.fcommands.proxy

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.proxy.data.ConfigValues
import com.github.fernthedev.fcommands.universal.BaseEvents
import com.github.fernthedev.fcommands.universal.EventCallback
import com.github.fernthedev.fernapi.universal.api.IFPlayer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ProxyEvents : BaseEvents() {

    @Inject
    protected lateinit var config: Config<ConfigValues>

    val onLeave: EventCallback<IFPlayer<*>> = EventCallback()

}