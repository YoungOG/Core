package org.ml.core.magic

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.ml.core.CorePlugin
import org.ml.core.profile.ProfileService
import java.text.DecimalFormat

abstract class MagicSpell(val name: String, val displayName: Component, val displayMaterial: Material) {
    abstract fun use(player: Player)
}

val SPELLS = arrayOf(
    BlinkSpell(),
    LeapSpell(),
    CrippleSpell()
)

fun Player.useSelectedSpell() {
    val plugin = CorePlugin.INSTANCE
    val profileService = plugin.injector.getInstance(ProfileService::class.java)

    val profile = profileService.getProfile(this.uniqueId)

    // TODO: Check for no-magic zones
    // TODO: Check for squad mates (can't de-buff friendly)
    // TODO: Use red stone
    // TODO: Apply cooldown

    val tick = plugin.server.currentTick
    if (profile.selectedSpell.cooldown > tick) {
        val rem = (profile.selectedSpell.cooldown - tick).toDouble() / 20.0
        this.sendMessage("§7Cooldown: §b${String.format("%.2f", rem)}s")
        return
    }

    val spell = SPELLS[profile.selectedSpell.index]
    spell.use(this)
    // TODO: Do not hardcode
    profile.selectedSpell.cooldown = tick + 100
}

fun Player.selectNextSpell() {
    val plugin = CorePlugin.INSTANCE
    val profileService = plugin.injector.getInstance(ProfileService::class.java)

    val profile = profileService.getProfile(this.uniqueId)

    for ((index, spell) in profile.spells.withIndex()) {
        if (spell.index != profile.selectedSpell.index) continue

        if (index >= profile.spells.size - 1) {
            profile.selectedSpell = profile.spells[0]
        } else {
            profile.selectedSpell = profile.spells[index + 1]
        }

        break
    }

    val spell = SPELLS[profile.selectedSpell.index]
    this.sendMessage("§7-> §b${spell.name}")
}

class MagicListeners : Listener {
    @EventHandler
    fun onUseWand(event: PlayerInteractEvent) {
        val item = event.player.inventory.itemInMainHand

        // TODO: More materials
        if (item.type == Material.STICK) {
            if (event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK) {
                event.player.useSelectedSpell()
            } else if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
                event.player.selectNextSpell()
            }
        }
    }
}