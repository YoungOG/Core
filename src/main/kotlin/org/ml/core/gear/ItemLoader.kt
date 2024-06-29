package org.ml.core.gear

import org.bukkit.inventory.ItemStack
import org.ml.core.CorePlugin
import java.util.*
import javax.inject.Singleton

@Singleton
class EpicItemService {

    private lateinit var corePlugin: CorePlugin

    private val items = hashMapOf<UUID, EpicItem>()
    private val itemStacks = hashMapOf<UUID, ItemStack>()

    fun initialize(corePlugin: CorePlugin): EpicItemService {
        this.corePlugin = corePlugin

        println("Loading epic items for ${corePlugin.name}: ${corePlugin.isEnabled}")


        // Load from TOML file
        return this
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



