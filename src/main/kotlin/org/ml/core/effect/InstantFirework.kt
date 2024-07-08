package org.ml.core.effect

import org.bukkit.FireworkEffect
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Firework
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

fun spawnInstantFirework(location: Location, effect: FireworkEffect) {
    val firework: Firework = location.world.spawnEntity(location, EntityType.FIREWORK_ROCKET) as Firework
    firework.isInvisible = true
    val meta = firework.fireworkMeta
    meta.addEffect(effect)
    firework.fireworkMeta = meta
    firework.detonate()

}

class InstantFireworkListeners : Listener {
    @EventHandler
    fun onFireworkDamage(event: EntityDamageEvent) {
        if (event.damageSource.directEntity is Firework)
            event.isCancelled = true
    }
}