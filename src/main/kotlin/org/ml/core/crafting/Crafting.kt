package org.ml.core.crafting

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.ml.core.config.ConfigService
import org.ml.core.gear.EpicItemService
import java.util.UUID

data class CraftingRecipe(
    // UUID of an epic item
    val result: UUID,
    val amount: Int,
    val components: List<CraftingComponent>
)

data class CraftingComponent(val epicItem: UUID?, val material: Material?, val amount: Int)

@Singleton
class CraftingService @Inject constructor(
    private val configService: ConfigService,
    private val itemService: EpicItemService,
) {
    private val recipes = arrayListOf<CraftingRecipe>()

    init {
        println("Requesting epic gear configs.")

        loadConfigs()
    }

    fun loadConfigs() {
        this.recipes.clear()

        val loadedItems = this.configService.createOrLoadConfigsInFolder<CraftingRecipe>("recipes")

        for (item in loadedItems) {
            this.recipes.add(item)
        }
    }

    fun findRecipe(items: ArrayList<ItemStack>): CraftingRecipe? {
        recipe@ for (recipe in recipes) {
            if (items.size != recipe.components.size) continue

            component@ for (component in recipe.components) {
                for (i in 0..<items.size) {
                    val stack = items[i]
                    if (matchesItem(component, stack)) {
                        continue@component
                    }
                }

                continue@recipe
            }

            return recipe
        }

        return null
    }

    fun matchesItem(component: CraftingComponent, itemStack: ItemStack): Boolean {
        if (itemStack.amount < component.amount)
            return false

        if (component.epicItem != null) {
            val epicItem = itemService.getItemFromStack(itemStack)
            if (epicItem != null && epicItem.id == component.epicItem)
                return true
        } else if (component.material != null) {
            if (itemStack.type == component.material)
                return true
        }

        return false
    }

    fun closeCraftingGUI(player: HumanEntity, inventory: Inventory) {
        if (inventory.holder !is CraftingGUI) return

        var droppedSome = false;

        forCraftingSlots { slot: Int ->
            val item = inventory.getItem(slot)
            if (item == null || item.type == Material.AIR) return@forCraftingSlots

            if (player.inventory.firstEmpty() == -1) {
                droppedSome = true
                player.world.dropItem(player.location, item)
            } else {
                player.inventory.addItem(item)
            }
        }

        if (droppedSome) {
            player.sendMessage("Some items were dropped because your inventory was full")
        }
    }
}