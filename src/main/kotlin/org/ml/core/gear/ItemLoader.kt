package org.ml.core.gear

import org.bukkit.inventory.ItemStack
import java.util.UUID

class EpicItemService {
    private val items = hashMapOf<UUID, EpicItem>()
    private val itemStacks = hashMapOf<UUID, ItemStack>()

    fun loadEpicItems(configString: String) {
        // Load from TOML file
    }

    fun getItemStack(item: EpicItem): ItemStack {
        return this.getItemStack(item.id)
    }

    fun getItemStack(itemId: UUID): ItemStack {
        return this.itemStacks.getOrElse(itemId) {
            ItemStack.empty()
        }
    }
}



