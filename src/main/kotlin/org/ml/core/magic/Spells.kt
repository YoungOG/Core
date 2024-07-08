package org.ml.core.magic

import net.axay.kspigot.chat.KColors
import net.axay.kspigot.chat.literalText
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.ml.core.effect.spawnInstantFirework

private val BLINK_EFFECT = FireworkEffect.builder()
    .withColor(Color.RED, Color.PURPLE)
    .with(FireworkEffect.Type.BURST)
    .withTrail()
    .withFlicker()
    .build()

private val LEAP_EFFECT = FireworkEffect.builder()
    .withColor(Color.YELLOW, Color.BLACK)
    .with(FireworkEffect.Type.BALL_LARGE)
    .withTrail()
    .withFlicker()
    .build()

class BlinkSpell : MagicSpell(
    "blink",
    literalText("Blink") {
        color = KColors.GOLD
    },
    Material.ENDER_PEARL
) {
    override fun use(player: Player) {
        val block = player.getTargetBlockExact(40)
        val face = player.getTargetBlockFace(40)

        if (block == null || face == null) {
            player.sendMessage("§cOut of range.")
            return
        }

        val playerLocation = player.location
        val safe = getSafeBlink(playerLocation, block, face)
        if (safe == null) {
            player.sendMessage("§cCannot blink there.")
            return
        }

        safe.yaw = playerLocation.yaw
        safe.pitch = playerLocation.pitch

        player.fallDistance = 0.0f
        player.teleport(safe)
        player.sendMessage("You blinked.")
        spawnInstantFirework(safe, BLINK_EFFECT)
    }
}

class LeapSpell : MagicSpell(
    "leap",
    literalText("Leap") {
        color = KColors.YELLOW
    },
    Material.FEATHER
) {
    override fun use(player: Player) {
        val v = player.location.direction
        player.velocity = v.setY(0.0).normalize().multiply(5.0).setY(2)
        player.sendMessage("You leaped.")
        spawnInstantFirework(player.location, LEAP_EFFECT)

    }
}

class CrippleSpell : MagicSpell(
    "cripple",
    literalText("Cripple") {
        color = KColors.GREEN
    },
    Material.SLIME_BALL
) {
    override fun use(player: Player) {
        player.sendMessage("You crippled.")
    }
}

fun getSafeBlink(from: Location, block: Block, blockFace: BlockFace): Location? {
    val world = from.world

    val blockLocation: Location = block.location
    var x: Int = blockLocation.blockX
    var y: Int = blockLocation.blockY
    var z: Int = blockLocation.blockZ

    if (blockFace == BlockFace.DOWN) {
        val down1: Boolean = isPathable(world, x, y - 1, z)
        val down2: Boolean = isPathable(world, x, y - 2, z)

        if (down1 && down2) return blockLocation.add(0.5, -2.0, 0.5)
    } else if (blockFace == BlockFace.UP) {
        val up1: Boolean = isPathable(world, x, y + 1, z)
        val up2: Boolean = isPathable(world, x, y + 2, z)

        if (up1 && up2) return blockLocation.add(0.5, 1.0, 0.5)
    } else {
        if (isPathable(world, x, y + 1, z) && isPathable(world, x, y + 2, z)) return blockLocation.add(0.5, 1.0, 0.5)

        val relative: Block = block.getRelative(blockFace)
        val relativeLocation: Location = relative.location
        x = relativeLocation.blockX
        y = relativeLocation.blockY
        z = relativeLocation.blockZ

        if (!isPathable(world, x, y, z)) return null

        val down1: Boolean = isPathable(world, x, y - 1, z)
        if (down1) return relativeLocation.add(0.5, -1.0, 0.5)

        val up1: Boolean = isPathable(world, x, y + 1, z)
        if (up1) return relativeLocation.add(0.5, 0.0, 0.5)
    }

    return null
}

//fun getSafeShadowstep(target: Entity): Location? {
//    val targetLocation: Location = target.getLocation()
//
//    val world = targetLocation.world
//    val directionNormalized: Vector = targetLocation.direction.normalize()
//    val x = (targetLocation.x - directionNormalized.getX()) as Int
//    val y: Int = targetLocation.blockY
//    val z = (targetLocation.z - directionNormalized.getZ()) as Int
//
//    if (!isPathable(world, x, y, z) || !isPathable(world, x, y + 1, z)) return findAnyAround(world, targetLocation)
//
//    return Location(
//        targetLocation.getWorld(),
//        x,
//        blockPosition.getY() - 1,
//        z
//    ).setDirection(targetLocation.direction)
//}

//private fun findAnyAround(world: WorldServer, location: Location): Location? {
//    val y: Int = location.getBlockY()
//    val yp: Int = location.getBlockY() + 1
//
//    for (x in location.getBlockX() - 1 until location.getBlockX() + 1) {
//        for (z in location.getBlockZ() - 1 until location.getBlockZ() + 1) {
//            if (!isPathable(world, x, y, z) || !isPathable(world, x, yp, z)) continue
//
//            return Location(location.getWorld(), x + 0.5, y, z + 0.5)
//        }
//    }
//
//    return null
//}

//private fun isPathable(world: WorldServer, x: Int, y: Int, z: Int): Boolean {
private fun isPathable(world: World, x: Int, y: Int, z: Int): Boolean {
    return !world.getBlockAt(x, y, z).isCollidable
}