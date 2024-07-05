package org.ml.core.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.tomlMapper
import com.google.inject.Inject
import com.google.inject.Singleton
import org.ml.core.CorePlugin
import java.io.File
import java.util.UUID

@Singleton
class ConfigService
@Inject
constructor(
    val corePlugin: CorePlugin,
) {

    val mapper = tomlMapper {
        decoder<TomlValue.String, UUID> { it: TomlValue.String ->
            UUID.fromString(it.value)
        }
        encoder<UUID> {
            TomlValue.String(it.toString())
        }
    }

    inline fun <reified T> createOrLoadConfigsInFolder(folderName: String): List<T> {
        val folder = File(this.corePlugin.dataFolder, folderName)
        if (!folder.exists()) {
            val creationResult = folder.mkdir()
            if (!creationResult) {
                throw Exception("Could not create $folderName HANDLE THIS SHIT")
            } else {
                return listOf()
            }
        }

        val loadedItems = arrayListOf<T>()
        folder.walkTopDown().filter {
            it.isFile && it.name.endsWith(".toml")
        }.forEach {
            val item = mapper.decode<T>(it.inputStream())
            loadedItems.add(item)
        }

        return loadedItems
    }
}
