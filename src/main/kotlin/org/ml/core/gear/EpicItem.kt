package org.ml.core.gear

import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import java.util.UUID

class EpicItem(
    val id: UUID,
    val name: String,
    val lore: ArrayList<String>?,
    val attributes: ArrayList<AttributePair>?,
    val enchantments: ArrayList<Enchantment>?,
) {
}


data class AttributePair(val attribute: Attribute, val modifier: AttributeModifier)

data class Enchantment(val type: EnchantmentType, val level: Int)

enum class EnchantmentType {
    SHARPNESS,
    PROTECTION,
}