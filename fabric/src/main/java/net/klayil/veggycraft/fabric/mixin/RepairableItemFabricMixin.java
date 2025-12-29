package net.klayil.veggycraft.fabric.mixin;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RepairableItemsExtension.class)
public abstract class RepairableItemFabricMixin implements FabricItem {
    @Override
    public ItemStack getRecipeRemainder(ItemStack stack) {
        return RepairableItemsExtension.getCraftingRemainderCommon(stack);
    }
}
