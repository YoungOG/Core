package org.ml.core.gear

import cc.ekblad.toml.TomlMapper
import cc.ekblad.toml.decode
import com.google.inject.Inject
import com.google.inject.Injector
import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.kspigot.commands.*
import java.util.*
import javax.inject.Singleton
import org.bukkit.inventory.ItemStack
import org.ml.core.CorePlugin
import org.ml.core.config.ConfigService

private data class EpicItemConfig(
    val item: List<EpicItem>
) {}

@Singleton
class EpicItemService @Inject constructor(
    corePlugin: CorePlugin,
    private val configService: ConfigService,
) {

    private val items = hashMapOf<UUID, EpicItem>()
    private val itemNames = hashMapOf<String, EpicItem>()
    private val itemStacks = hashMapOf<UUID, ItemStack>()

    init {
        println("Requesting epic gear configs.")

        loadConfigs()
    }

    fun loadConfigs() {
        this.items.clear()
        this.itemNames.clear()
        this.itemStacks.clear()

        val loadedItems = this.configService.createOrLoadConfigsInFolder<EpicItemConfig>("items")

        for (itemList in loadedItems) {
            for (item in itemList.item) {
                this.items[item.id] = item
                this.itemNames[item.name] = item
                this.itemStacks[item.id] = epicItemToItemStack(item)
                println("Loaded item ${item.name}")
                //        this.itemStacks[item.id] =
            }
        }
    }

    fun getItemStack(item: EpicItem): ItemStack {
        return this.getItemStack(item.id)
    }

    fun getItemStack(itemId: UUID): ItemStack {
        return this.itemStacks.getOrElse(itemId) { ItemStack.empty() }
    }

    fun getItemByName(name: String): EpicItem? {
        return this.itemNames[name]
    }

    fun getItemNamesList(): List<String> {
        return this.itemNames.keys.toList()
    }
}

fun registerEpicItemCommands(injector: Injector) {
    val epicItemService = injector.getInstance(EpicItemService::class.java)
    command("gear") {
        literal("reload") {
            runs {
                epicItemService.loadConfigs()
                player.sendMessage("Reloaded config")
            }
        }

        literal("give") {
            argument("item", StringArgumentType.string()) {
                suggestList {
                    epicItemService.getItemNamesList()
                }
                runs {
                    val name = getArgument<String>("item")
                    val item = epicItemService.getItemByName(name)
                    if (item == null) {
                        player.sendMessage("Item not found: $name")
                        return@runs
                    }

                    val itemStack = epicItemService.getItemStack(item)
                    player.inventory.addItem(itemStack)
                    player.sendMessage("Added $itemStack")
                }
            }

        }
    }

    reloadCommand(injector)
}

fun reloadCommand(injector: Injector) {}
