package org.ml.core.magic

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.ml.core.CorePlugin
import org.ml.core.profile.ProfileService

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

    val spell = SPELLS[profile.selectedSpell]
    spell.use(this)
}

fun Player.selectNextSpell() {
    val plugin = CorePlugin.INSTANCE
    val profileService = plugin.injector.getInstance(ProfileService::class.java)

    val profile = profileService.getProfile(this.uniqueId)

    for ((index, spell) in profile.spells.withIndex()) {
        if (spell != profile.selectedSpell) continue

        if (index >= profile.spells.size - 1) {
            profile.selectedSpell = profile.spells[0]
        } else {
            profile.selectedSpell = profile.spells[index + 1]
        }

        break
    }

    val spell = SPELLS[profile.selectedSpell]
    this.sendMessage("You have activated ${spell.name}")
}
