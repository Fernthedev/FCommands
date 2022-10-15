package com.github.fernthedev.fcommands.spigot.feature

import com.github.fernthedev.config.common.Config
import com.github.fernthedev.fcommands.spigot.NewSpigotConfig
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.delay
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.Plugin
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FernProtection @Inject constructor(
    private val plugin: Plugin,
    private val config: Config<NewSpigotConfig>,
) : Listener {

    @EventHandler
    fun onFernBreak(e: BlockBreakEvent) {
        if (!config.configData.fernBreakProtection) return
        if (e.block.type != Material.FERN) return
        if (e.player.itemInUse?.type == Material.SHEARS || e.player.itemInUse?.containsEnchantment(Enchantment.SILK_TOUCH) == true) return
        if (e.player.hasPermission("fernc.fernbreak.ignore")) return

        plugin.launch {
            strikeLightningLoop(e.player)
        }
    }

    @EventHandler
    fun onFernBlockReplace(e: BlockPlaceEvent) {
        if (!config.configData.fernBreakProtection) return
        if (!e.canBuild()) return
        if (e.blockReplacedState.block.type != Material.FERN) return
        if (e.player.hasPermission("fernc.fernbreak.ignore")) return

        plugin.launch {
            strikeLightningLoop(e.player)
        }
    }

    private suspend fun strikeLightningLoop(player: Player) {
        player.sendMessage("You damaged a Fern! Suffer the consequences")

        for (i in 0..3) {
            if (!player.isValid) return

            player.world.strikeLightning(player.location)

            delay(200)
        }

    }

}