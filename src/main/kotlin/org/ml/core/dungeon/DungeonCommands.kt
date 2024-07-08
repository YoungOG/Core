package org.ml.core.dungeon

import com.google.inject.Injector
import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.kspigot.commands.*
import org.ml.core.crafting.openCraftingGUI
import org.ml.core.gear.EpicItemService
import org.ml.core.gear.openGearGUI
import org.ml.core.gear.reloadCommand

fun registerDungeonCommands(injector: Injector) {
    val epicItemService = injector.getInstance(EpicItemService::class.java)
    command("gear") {
        literal("reload") {
            runs {
                epicItemService.loadConfigs()
                player.sendMessage("Reloaded config")
            }
        }

        literal("menu") {
            runs {
                openGearGUI(player)
            }
        }

        literal("craft") {
            runs {
                openCraftingGUI(player)
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
                    if (itemStack == null) {
                        // TODO
                        player.sendMessage("NOT FOUND YO FIX THIS")
                        return@runs
                    }

                    player.inventory.addItem(itemStack)
                    player.sendMessage("Added $itemStack")
                }
            }

        }
    }

    reloadCommand(injector)
}