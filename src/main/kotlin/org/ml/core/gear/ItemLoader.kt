package org.ml.core.gear

import cc.ekblad.toml.decode
import com.google.inject.Inject
import com.google.inject.Injector
import java.util.*
import javax.inject.Singleton
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.literal
import net.axay.kspigot.commands.runs
import org.bukkit.inventory.ItemStack
import org.ml.core.CorePlugin
import org.ml.core.config.ConfigService

@Singleton
class EpicItemService @Inject constructor(
    corePlugin: CorePlugin,
    private val configService: ConfigService,
) {

    private val items = hashMapOf<UUID, EpicItem>()
    private val itemStacks = hashMapOf<UUID, ItemStack>()

    init {
        print("Requesting epic gear configs.")

    }


    fun loadItem(config: String): EpicItem {
        val item = configService.mapper.decode<EpicItem>(config)

        this.items[item.id] = item
//        this.itemStacks[item.id] =

        return item
    }

    fun getItemStack(item: EpicItem): ItemStack {
        return this.getItemStack(item.id)
    }

    fun getItemStack(itemId: UUID): ItemStack {
        return this.itemStacks.getOrElse(itemId) { ItemStack.empty() }
    }
}

fun registerEpicItemCommands(injector: Injector) {
    command("gear") {
        literal("reload") {
            runs {
                print("Reload")
                print(" here")
            }
        }
    }

    reloadCommand(injector)
}

fun reloadCommand(injector: Injector) {}
