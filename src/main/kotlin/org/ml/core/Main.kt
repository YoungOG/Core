package org.ml.core

import com.google.inject.Guice
import com.google.inject.Injector
import javax.inject.Inject
import net.axay.kspigot.commands.*
import net.axay.kspigot.main.KSpigot
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.core.UUIDUtil
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.util.ExtraCodecs
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.event.Listener
import org.bukkit.inventory.CraftingInventory
import org.ml.core.crafting.CraftingGUI
import org.ml.core.crafting.CraftingListeners
import org.ml.core.crafting.CraftingService
import org.ml.core.gear.registerEpicItemCommands
import org.ml.core.magic.openSpellGUI
import org.ml.core.magic.selectNextSpell
import org.ml.core.magic.useSelectedSpell
import org.ml.core.profile.ProfileListeners
import java.io.File
import java.util.UUID
import java.util.function.UnaryOperator

class CorePlugin : KSpigot() {

    companion object {
        lateinit var INSTANCE: CorePlugin
            private set
    }

    @Inject
    lateinit var injector: Injector

    override fun load() {
        INSTANCE = this
    }

    override fun shutdown() {
        // Refund items in crafting inventory
        for (player in Bukkit.getOnlinePlayers()) {
            val craftingService = injector.getInstance(CraftingService::class.java)
            val inventory = player.openInventory.topInventory
            if (inventory.holder !is CraftingGUI) continue

            craftingService.closeCraftingGUI(player, inventory)
        }
    }

    override fun startup() {
        if (!this.dataFolder.exists()) {
            println("${this.dataFolder.absolutePath} doesn't exist!")
            val creationResult = this.dataFolder.mkdir()
            if (!creationResult) {
                throw Exception("Could not create file HANDLE THIS SHIT")
            }
        }


        Bukkit.getLogger().info("Plugin enabled!")

        injector = Guice.createInjector(CoreMainModule(this))
        injector.injectMembers(this)

        listener(ProfileListeners())
        listener(CraftingListeners())

        setupCommands()
    }

    private fun setupCommands() {
        command("squad") {
            literal("create") {
                runs {
                    this.sender.bukkitSender.sendMessage("hey gamer ;)")
                }
            }
        }

        command("spell") {
            literal("next") {
                runs {
                    player.selectNextSpell()
                }
            }

            literal("use") {
                runs {
                    player.useSelectedSpell()
                }
            }

            literal("menu") {
                runs {
                    openSpellGUI(player)
                }
            }
        }

        registerEpicItemCommands(this.injector)
    }

    private fun listener(listener: Listener) {
        val pm = Bukkit.getPluginManager()

        injector.injectMembers(listener)

        pm.registerEvents(listener, this)
    }
}
