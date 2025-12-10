package net.klayil.veggycraft.item;

import net.minecraft.world.item.Item;

public class UnrepairableDamageableItems extends net.klayil.veggycraft.item.RepairableItemsExtension {
    public UnrepairableDamageableItems(Item.Properties itemProperties, int maxDamage) {
        super(itemProperties, maxDamage, null);
    }

//    @Override
//    public boolean canRepair(ItemStack stack, ItemStack ingredient) {return false;}
}