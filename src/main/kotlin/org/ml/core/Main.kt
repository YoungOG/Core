package org.ml.core

import net.axay.kspigot.commands.*
import net.axay.kspigot.main.KSpigot
import org.bukkit.Bukkit

class CorePlugin : KSpigot() {

    companion object {
        lateinit var INSTANCE: CorePlugin
            private set
    }

    override fun load() {
        INSTANCE = this
    }

    override fun startup() {
        val plugin = this

        command("squad") {
            literal("create") {
                runs {
                   print("Hello!")
                }
            }
        }

        Bukkit.getLogger().info("YOOOOOOO!")
    }
}

val Manager by lazy { CorePlugin.INSTANCE }