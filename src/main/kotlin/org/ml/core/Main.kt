package org.ml.core

import com.google.inject.Guice
import com.google.inject.Injector
import javax.inject.Inject
import net.axay.kspigot.commands.*
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

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
        injector = Guice.createInjector(CoreMainModule(this))
        injector.injectMembers(this)

        setupCommands()
        Bukkit.getLogger().info("Plugin enabled!")
    }

    private fun setupCommands() {
        command("squad") {
            literal("create") { runs { this.sender.bukkitSender.sendMessage("hey gamer ;)") } }
        }
    }
}
