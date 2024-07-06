package org.ml.core.config

import cc.ekblad.toml.decode
import cc.ekblad.toml.encodeTo
import cc.ekblad.toml.model.TomlValue
import cc.ekblad.toml.tomlMapper
import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.enchantments.Enchantment
import org.ml.core.CorePlugin
import org.ml.core.profile.Profile
import java.io.File
import java.util.UUID

@Singleton
class ConfigService
@Inject
constructor(
    private val corePlugin: CorePlugin,
) {
    val mapper = tomlMapper {
        decoder<TomlValue.String, UUID> { it: TomlValue.String ->
            UUID.fromString(it.value)
        }
        encoder<UUID> {
            TomlValue.String(it.toString())
        }
        decoder<TomlValue.String, Enchantment> { it: TomlValue.String ->
            Enchantment.getByName(it.value)
        }
        encoder<Enchantment> {
            TomlValue.String(it.name)
        }
    }

    fun ensureFolderExists(folderName: String): File? {
        val file = File(this.corePlugin.dataFolder, folderName)
        if (!file.exists()) {
            val result = file.mkdirs()
            if (!result) {
                return null
            }

        }

        return file;
    }

    inline fun <reified T> createOrLoadConfigsInFolder(folderName: String): List<T> {
        val folder = ensureFolderExists(folderName) ?: throw Exception("Could not create $folderName HANDLE THIS SHIT")

        val loadedItems = arrayListOf<T>()
        folder.walkTopDown().filter {
            it.isFile && it.name.endsWith(".toml")
        }.forEach {
            val item = mapper.decode<T>(it.inputStream())
            loadedItems.add(item)
        }

        return loadedItems
    }

    fun loadProfile(id: UUID): Profile? {
        val file = File(this.corePlugin.dataFolder, "profiles/${id}.toml")
        if (!file.exists()) {
            return null
        }

        return mapper.decode<Profile>(file.inputStream())
    }

    fun saveProfile(profile: Profile) {
        val folder = ensureFolderExists("profiles") ?: throw Exception("Could not create HANDLE THIS SHIT")
        val file = File(folder, "${profile.id}.toml")
        if (!file.exists()) {
            file.createNewFile()
        }

        mapper.encodeTo(file.outputStream(), profile)
    }

}


