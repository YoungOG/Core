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
import org.bukkit.event.Listener
import org.ml.core.gear.registerEpicItemCommands
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

        setupCommands()
    }

    private fun setupCommands() {
        command("squad") {
            literal("create") { runs { this.sender.bukkitSender.sendMessage("hey gamer ;)") } }
        }

        registerEpicItemCommands(this.injector)
    }

    private fun listener(listener: Listener) {
        val pm = Bukkit.getPluginManager()

        injector.injectMembers(listener)

        pm.registerEvents(listener, this)
    }
}
