package org.ml.core.profile

import com.google.inject.Inject
import com.google.inject.Singleton
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPreLoginEvent
import org.ml.core.config.ConfigService
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

// TODO: Spells, kits?, stats (kills, deaths etc),
data class Profile(
    val id: UUID,
    var name: String,
    val ignoredPlayers: List<UUID>?,
) {

    constructor(id: UUID, name: String) : this(id, name, null)
}

@Singleton
class ProfileService @Inject constructor(private val configService: ConfigService) {
    private var profiles = hashMapOf<UUID, Profile>()
    private var loadingProfiles = ConcurrentHashMap<UUID, Profile>()

    fun loadProfile(id: UUID, name: String) {
        var profile = configService.loadProfile(id)
        if (profile == null) {
            profile = Profile(id, name)
            configService.saveProfile(profile)
        }

        profile.name = name

        println("Loaded profile ${profile.name}")

        loadingProfiles[id] = profile
    }

    fun transferProfile(id: UUID) {
        val profile = loadingProfiles.remove(id) ?: throw Exception("HANDLE")

        profiles[id] = profile
    }
}

class ProfileListeners : Listener {

    @Inject
    lateinit var profileService: ProfileService

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerPreJoin(event: AsyncPlayerPreLoginEvent) {
        if (event.result == PlayerPreLoginEvent.Result.ALLOWED)
            profileService.loadProfile(event.uniqueId, event.name)
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        profileService.transferProfile(event.player.uniqueId)
    }
}