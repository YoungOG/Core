package org.ml.core.gear

import net.axay.kspigot.items.addLore
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.inventory.ItemStack
import java.util.UUID

class EpicItem(
    val id: UUID,
    val name: String,
    val material: Material,
    val displayName: String?,
    val lore: List<String>?,
    val attributes: List<AttributePair>?,
    val enchantments: List<Enchantment>?,
) {
}


data class AttributePair(val attribute: Attribute, val modifier: AttributeModifier)

data class Enchantment(val type: org.bukkit.enchantments.Enchantment, val level: Int)

data class AttributeModifier(val id: UUID, val amount: Double, val operation: Operation, val slot: AttributeSlot)

enum class AttributeSlot {
    ANY,
    MAINHAND,
    OFFHAND,
    HAND,
    FEET,
    LEGS,
    CHEST,
    HEAD,
    ARMOR,
    BODY,
}

fun epicItemToItemStack(epicItem: EpicItem): ItemStack {
    val item = ItemStack(epicItem.material)

    val meta = item.itemMeta;

    if (epicItem.displayName != null) {
        meta.displayName(Component.text(epicItem.displayName))
    }

    if (epicItem.lore != null) {
        for (lore in epicItem.lore) {
            meta.addLore {
                +Component.text(lore)
            }
        }
    }

    if (epicItem.attributes != null) {
        for (attribute in epicItem.attributes) {
            val slot = org.bukkit.inventory.EquipmentSlotGroup.getByName(attribute.modifier.slot.name.toLowerCase())
                ?: continue

            meta.addAttributeModifier(attribute.attribute, org.bukkit.attribute.AttributeModifier(
                attribute.modifier.id,
                "",
                attribute.modifier.amount,
                attribute.modifier.operation,
               slot,
            ))
        }
    }

    if (epicItem.enchantments != null) {
        for (enchantment in epicItem.enchantments) {
            meta.addEnchant(enchantment.type, enchantment.level, true)
        }
    }

    item.itemMeta = meta

    return item
}