package org.ml.core.gear

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.gui.*
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.ml.core.CorePlugin
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun openGearGUI(player: Player) {
    val plugin = CorePlugin.INSTANCE;
    val gearService = plugin.injector.getInstance(EpicItemService::class.java)

    val gui = kSpigotGUI(GUIType.SIX_BY_NINE) {
        title = literalText("Gear Menu") { color = KColors.AQUA }
        page(1) {
            val compound = createRectCompound<EpicItem>(
                Slots.RowTwoSlotOne, Slots.RowSixSlotNine,
                iconGenerator = {
                    val stack = gearService.getItemStack(it.id)?: return@createRectCompound ItemStack.empty()
//                    val stack = epicItemToItemStack(it)
                    cloneStack(stack) {
                        meta {
                            addLore {
                                +" "
                                +"§8§oClick to obtain"
                            }
                        }
                    }
                },
                onClick = { clickEvent, element ->
                    player.chat("/gear give ${element.name}")
                    clickEvent.bukkitEvent.isCancelled = true
                }
            )

            compound.addContent(gearService.getEpicItemsList())
//            for (i in 0..200) {
//                val mat = Material.values()[ThreadLocalRandom.current().nextInt(Material.values().size - 1) + 1]
//                compound.addContent(EpicItem(UUID.randomUUID(), "test name", mat, "Item $i", null, null, null))
//            }

            compoundScroll(
                Slots.RowOneSlotEight,
                itemStack(Material.PAPER) {
                    meta {
                        name = literalText("Previous page <<") { color = KColors.GOLD }
                    }
                }, compound, scrollTimes = 5, reverse = true
            )

            compoundScroll(
                Slots.RowOneSlotNine,
                itemStack(Material.PAPER) {
                    meta {
                        name = literalText("Next page >>") { color = KColors.GOLD }
                    }
                }, compound, scrollTimes = 5
            )

        }
    }

    player.openGUI(gui)
}