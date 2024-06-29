package org.ml.core.gear

import org.bukkit.inventory.ItemStack
import org.ml.core.CorePlugin
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EpicItemService @Inject constructor(private val corePlugin: CorePlugin) {

    private val items = hashMapOf<UUID, EpicItem>()
    private val itemStacks = hashMapOf<UUID, ItemStack>()

    init {
        loadEpicItems("yourConfigString") // Call loadEpicItems here with the appropriate config string
    }

    fun loadEpicItems(configString: String) {
        println("Loading epic items from: $configString, corePlugin: ${corePlugin.name}, enabled: ${corePlugin.isEnabled}")
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



