package com.github.fernthedev.fcommands.bungee.modules

import com.github.fernthedev.fcommands.bungee.BungeeEvents
import com.github.fernthedev.fcommands.universal.BaseEvents
import com.google.inject.AbstractModule

class BungeeEventsModule : AbstractModule() {

    override fun configure() {
        val classes = mutableListOf<Class<out BaseEvents>>(BungeeEvents::class.java)


        var parentKlass: Class<*>? = BungeeEvents::class.java.superclass;
        while (parentKlass != null && BungeeEvents::class.java.isAssignableFrom(parentKlass)) {
            classes.add(parentKlass as Class<out BaseEvents>)
            parentKlass = parentKlass.superclass
        }

        classes.forEach { clazz ->
            bind(clazz) to BungeeEvents::class.java
        }
    }
}