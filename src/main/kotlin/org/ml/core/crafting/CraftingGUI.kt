package org.ml.core.crafting

import com.google.inject.Inject
import net.axay.kspigot.chat.literalText
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.firstSync
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.ml.core.gear.EpicItemService

class CraftingListeners : Listener {

    @Inject
    lateinit var craftingService: CraftingService

    @Inject
    lateinit var itemService: EpicItemService

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: event.inventory
        if (inventory.holder !is CraftingGUI) {
            if (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                val openInventory = event.whoClicked.openInventory.topInventory
                if (openInventory.holder is CraftingGUI)
                    updateInventory(openInventory)
            }

            return
        }

        val withinCraftArea = (event.slot in 10..14) || (event.slot in 19..23) || (event.slot in 28..32)
        val withinResult = event.slot == 25

        if (withinResult) {
            val resultItem = inventory.getItem(25)

            if (resultItem == null || resultItem.type == Material.AIR) {
                event.isCancelled = true
            } else {
                payIngredients(inventory)
            }
        } else if (withinCraftArea) {
            updateInventory(inventory)
        } else {
            event.isCancelled = true
        }
    }

    private fun updateInventory(inventory: Inventory) {
        firstSync {
            val items = arrayListOf<ItemStack>()
            forCraftingSlots {
                val item = inventory.getItem(it)
                if (item != null && item.type != Material.AIR) {
                    items.add(item)
                }
            }

            val recipe = craftingService.findRecipe(items)
            if (recipe == null) {
                inventory.setItem(25, ItemStack.empty())
            } else {
                val result = itemService.getItemStack(recipe.result) ?: return@firstSync
                result.amount = recipe.amount
                inventory.setItem(25, result)
            }
        }.execute()
    }

    private fun payIngredients(inventory: Inventory) {
        val items = arrayListOf<ItemStack>()
        forCraftingSlots {
            val item = inventory.getItem(it)
            if (item != null && item.type != Material.AIR) {
                items.add(item)
            }
        }

        val recipe = craftingService.findRecipe(items) ?: return

        for (component in recipe.components) {
            forCraftingSlots {
                val item = inventory.getItem(it)
                if (item != null && item.type != Material.AIR && craftingService.matchesItem(component, item)) {
                    item.amount -= component.amount
                    if (item.amount <= 0) {
                        inventory.setItem(it, ItemStack(Material.AIR))
                    }
                }
            }
        }

        updateInventory(inventory)

    }

    @EventHandler
    fun onDrag(event: InventoryDragEvent) {
        if (event.inventory.holder is CraftingGUI)
            event.isCancelled = true
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        if (event.inventory.holder !is CraftingGUI) return

        craftingService.closeCraftingGUI(event.player, event.inventory)
    }
}

fun forCraftingSlots(run: (Int) -> Unit) {
    for (slot in 10..14) {
        run(slot)
    }

    for (slot in 19..23) {
        run(slot)
    }

    for (slot in 28..32) {
        run(slot)
    }
}

class CraftingGUI : InventoryHolder {
    override fun getInventory(): Inventory {
        throw Exception("IGNORED")
    }
}

private val BORDER = itemStack(Material.BLACK_STAINED_GLASS_PANE) {
    meta {
        name = literalText(" ")
    }
}

fun openCraftingGUI(player: Player) {
    val inventory = Bukkit.createInventory(CraftingGUI(), 45, "§bCrafting Menu")

    for (i in 0..9) {
        inventory.setItem(i, BORDER)
    }

    for (i in 34..44) {
        inventory.setItem(i, BORDER)
    }

    inventory.setItem(17, BORDER)
    inventory.setItem(18, BORDER)
    inventory.setItem(26, BORDER)
    inventory.setItem(27, BORDER)

    inventory.setItem(24, BORDER)
    inventory.setItem(15, BORDER)
    inventory.setItem(33, BORDER)
    inventory.setItem(16, BORDER)

    player.openInventory(inventory)
}