package org.ml.core

import com.google.inject.Guice
import com.google.inject.Injector
import javax.inject.Inject
import net.axay.kspigot.commands.*
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit
import org.ml.core.gear.registerEpicItemCommands
import java.io.File

class CorePlugin : KSpigot() {

    companion object {
        lateinit var INSTANCE: CorePlugin
            private set
    }

    @Inject lateinit var injector: Injector

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

        setupCommands()
    }

    private fun setupCommands() {
        command("squad") {
            literal("create") { runs { this.sender.bukkitSender.sendMessage("hey gamer ;)") } }
        }

        registerEpicItemCommands(this.injector)
    }
}
