package com.github.fernthedev.fcommands.spigot.feature

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.spigot.NewSpigotConfig
import org.bukkit.entity.Egg
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.ProjectileHitEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnowballFix @Inject constructor(
    private val config: Config<NewSpigotConfig>,
) : Listener {

    @EventHandler
    fun onSnowballHit(e: ProjectileHitEvent) {
        if (!config.configData.snowball) return
        val entity = e.entity
        if (entity !is Snowball && entity !is Egg) return

        val hitPlayer = e.hitEntity
        if (hitPlayer !is Player) return


        if (!hitPlayer.isValid || hitPlayer.isInvulnerable || hitPlayer.isDead) return

        hitPlayer.velocity.x = 0.0
        hitPlayer.velocity.y = 0.0
        hitPlayer.damage(0.01, entity)
    }

}