package com.github.fernthedev.fcommands.universal

class EventCallback<T> {
    private val callbacks: MutableMap<Any, (T) -> Unit> = HashMap()

    fun register(instance: Any,callback: (T) -> Unit) {
        callbacks[instance] = callback

    }

    fun erase(instance: Any) {
        callbacks.remove(instance)
    }

    operator fun invoke(t: T) {
        callbacks.forEach { (_, callback) ->
            callback(t)
        }
    }
}