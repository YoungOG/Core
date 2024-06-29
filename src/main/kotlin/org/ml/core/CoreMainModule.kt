package org.ml.core

import com.google.inject.AbstractModule
import org.ml.core.gear.EpicItemService

class CoreMainModule(private val plugin: CorePlugin) : AbstractModule() {
    override fun configure() {
        bind(CorePlugin::class.java).toInstance(plugin)
        bind(EpicItemService::class.java).asEagerSingleton()

        // Additional bindings can be defined here
    }

//    @Provides
//    @Singleton
//    fun provideFileConfiguration(): FileConfiguration {
//        val configFile = File(plugin.dataFolder, "config.yml")
//        return YamlConfiguration.loadConfiguration(configFile)
//    }
}