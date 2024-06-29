package org.ml.core

import com.google.inject.AbstractModule
import org.ml.core.gear.EpicItemService
import org.ml.core.network.CrossServerService

class CoreMainModule(private val plugin: CorePlugin) : AbstractModule() {
    override fun configure() {
        bind(CorePlugin::class.java).toInstance(plugin)
        bind(CrossServerService::class.java).asEagerSingleton()
        bind(EpicItemService::class.java).asEagerSingleton()

        // Additional bindings can be defined here
    }
}