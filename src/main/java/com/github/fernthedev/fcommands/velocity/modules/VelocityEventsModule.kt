package com.github.fernthedev.fcommands.velocity.modules

import com.github.fernthedev.fcommands.universal.BaseEvents
import com.github.fernthedev.fcommands.velocity.VelocityEvents
import com.google.inject.AbstractModule

class VelocityEventsModule : AbstractModule() {

    override fun configure() {
        val classes = mutableListOf<Class<out BaseEvents>>(VelocityEvents::class.java)


        var parentKlass: Class<*>? = VelocityEvents::class.java.superclass;
        while (parentKlass != null && VelocityEvents::class.java.isAssignableFrom(parentKlass)) {
            classes.add(parentKlass as Class<out BaseEvents>)
            parentKlass = parentKlass.superclass
        }

        val typeLiteral: Class<out Nothing> = VelocityEvents::class.java.asSubclass(Nothing::class.java);

        classes.forEach { clazz ->
            bind(clazz).to(typeLiteral)
        }
    }
}