package com.github.fernthedev.fcommands.spigot

import org.bukkit.Bukkit
import org.bukkit.Location

data class LocationJSON(
    var world: String,
    var x: Double = 0.0,
    var y: Double = 0.0,
    var z: Double = 0.0,
    var pitch: Float = 0f,
    var yaw: Float = 0f
) {
    fun toLocation(): Location {
        return Location(Bukkit.getWorld(world), x, y, z, pitch, yaw)
    }
}

data class HeavenData(
    var enabled: Boolean = true,
    var heavenLocation: LocationJSON = LocationJSON("world"),
    var hellLocation: LocationJSON = LocationJSON("nether"),
    var councilLocation: LocationJSON = LocationJSON("world"),
)

data class NewSpigotConfig(
    var heaven: HeavenData = HeavenData(),
    var rideBow: Boolean = true,
    var fernBreakProtection: Boolean = true,
)