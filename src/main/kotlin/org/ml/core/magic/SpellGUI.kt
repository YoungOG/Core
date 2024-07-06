package org.ml.core.magic

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.entity.Player

fun openSpellGUI(player: Player) {
    val gui = kSpigotGUI(GUIType.THREE_BY_NINE) {
        title = literalText("Spell Menu") { color = KColors.AQUA }
        page(1) {
            val compound = createRectCompound<MagicSpell>(
                Slots.RowOneSlotOne, Slots.RowThreeSlotEight,
                iconGenerator = {
                    itemStack(it.displayMaterial) {
                        meta {
                            name = it.displayName
                            addLore {
                                +"§7Learned"
                            }
                        }
                    }
                },
                onClick = { clickEvent, element ->
                    player.sendMessage("You clicked ${element.name}")
                    clickEvent.bukkitEvent.isCancelled = true
                }
            )

            compound.addContent(SPELLS.toList())
        }
    }

    player.openGUI(gui)
}