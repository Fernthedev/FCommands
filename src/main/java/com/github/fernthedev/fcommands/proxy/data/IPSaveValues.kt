package com.github.fernthedev.fcommands.proxy.data

import java.util.*


data class IPSaveValues(
    val playerMap: MutableMap<String, List<UUID>> = HashMap()
) {
    fun removePlayer(ip: String) {
        for (key in playerMap.keys) {
            if (key == ip) playerMap.remove(key)
        }
    }

    fun getPlayers(ip: String): List<UUID>? {
        for (key in playerMap.keys) {
            if (key == ip) return playerMap[key]
        }
        return null
    }
}