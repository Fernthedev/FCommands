package com.github.fernthedev.fcommands.spigot.feature

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.spigot.NewSpigotConfig
import com.github.fernthedev.fernapi.universal.Universal
import com.github.fernthedev.fernapi.universal.api.IFPlayer
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class Heaven @Inject constructor(
    private val config: Config<NewSpigotConfig>,
    private val plugin: Plugin
) : Listener {

    private var playersWhoDied = HashMap<Player, Boolean>()

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerDeath(event: PlayerRespawnEvent) {
        if (!config.configData.heaven.enabled) return

        val player = event.player

        if (!player.hasPermission("fernc.heaven.spawn")) return


        if (playersWhoDied.containsKey(player)) {
            val heaven = playersWhoDied[player]!!
            val location =
                if (heaven) config.configData.heaven.heavenLocation.toLocation()
                else config.configData.heaven.hellLocation.toLocation()

            event.respawnLocation = location
        } else {
            event.respawnLocation = config.configData.heaven.councilLocation.toLocation()


            plugin.launch {
                val heaven = playersWhoDied[player] ?: coroutineScope {
                    val v = lokiSpeak(Universal.getMethods().convertPlayerObjectToFPlayer(player))
                    playersWhoDied[player] = v
                    delay(1500)
                    v
                }

                val location =
                    if (heaven) config.configData.heaven.heavenLocation.toLocation()
                    else config.configData.heaven.hellLocation.toLocation()

                player.teleport(location)
            }
        }
    }

    fun resetChance() {
        playersWhoDied = HashMap()
    }


    /**
     * @return true if heaven, false if hell
     */
    private suspend fun lokiSpeak(player: IFPlayer<Player>): Boolean {
        delay(3000)

        val lokiSays = { Component.text("Loki: ").color(NamedTextColor.YELLOW) }

        player.sendMessage(
            lokiSays()
                .append(
                    Component.text("WUAH WUAH So, you have died in the overworld WUAH").color(NamedTextColor.GRAY)
                )
        )
        delay(2000)
        player.sendMessage(
            lokiSays()
                .append(
                    Component.text("I will now decide your fate").color(NamedTextColor.RED)
                )
        )

        delay(300)
        for (i in 0..4) {
            delay(800 + (i * 630).toLong())
            player.sendMessage(
                lokiSays()
                    .append(
                        Component.text("Eats many seebs").color(NamedTextColor.DARK_GRAY).decorate(TextDecoration.ITALIC)
                    )
            )
        }

        delay(1300)

        val maxValue = 1000;

        // 50% chance
        val gotoHeaven = Random.Default.nextInt(0, maxValue) < maxValue / 2f

        player.sendMessage(
            lokiSays()
                .append(
                    Component.text("It's been decided").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                )
        )
        delay(1500)

        if (gotoHeaven) {
            player.sendMessage(
                lokiSays()
                    .append(
                        Component.text("You will go to heaven CHIRP CHIRP").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)
                    )
            )
        } else {
            player.sendMessage(
                lokiSays()
                    .append(
                        Component.text("You will go to hell HISS HISS").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)
                    )
            )
        }

        return gotoHeaven
    }

}