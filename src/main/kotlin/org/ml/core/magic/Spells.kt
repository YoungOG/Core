package org.ml.core.magic

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import org.bukkit.Material
import org.bukkit.entity.Player

class BlinkSpell : MagicSpell(
    "blink",
    literalText("Blink") {
        color = KColors.GOLD
    },
    Material.ENDER_PEARL
) {
    override fun use(player: Player) {
        player.sendMessage("You blinked.")
    }
}

class LeapSpell : MagicSpell(
    "leap",
    literalText("Leap") {
        color = KColors.YELLOW
    },
    Material.FEATHER
) {
    override fun use(player: Player) {
        player.sendMessage("You leaped.")
    }
}

class CrippleSpell : MagicSpell(
    "cripple",
    literalText("Cripple") {
        color = KColors.GREEN
    },
    Material.SLIME_BALL
) {
    override fun use(player: Player) {
        player.sendMessage("You crippled.")
    }
}