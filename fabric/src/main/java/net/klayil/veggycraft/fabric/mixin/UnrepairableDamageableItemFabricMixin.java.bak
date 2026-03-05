package net.klayil.veggycraft.fabric.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.klayil.veggycraft.item.UnrepairableDamageableItems;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UnrepairableDamageableItems.class)
public abstract class UnrepairableDamageableItemFabricMixin implements FabricItem {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return UnrepairableDamageableItems.getCraftingRemainderCommon(stack);
    }
}
