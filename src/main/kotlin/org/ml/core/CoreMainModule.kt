package org.ml.core

import cc.ekblad.toml.TomlMapper
import cc.ekblad.toml.tomlMapper
import com.google.inject.AbstractModule
import org.ml.core.gear.EpicItemService
import org.ml.core.profile.ProfileService

class CoreMainModule(private val plugin: CorePlugin) : AbstractModule() {
    override fun configure() {
        bind(CorePlugin::class.java).toInstance(plugin)
        bind(TomlMapper::class.java).toInstance(tomlMapper {})
        bind(EpicItemService::class.java).asEagerSingleton()
        bind(ProfileService::class.java).asEagerSingleton()

        // Additional bindings can be defined here
    }
}