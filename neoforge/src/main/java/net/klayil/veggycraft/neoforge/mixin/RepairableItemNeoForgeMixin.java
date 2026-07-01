package net.klayil.veggycraft.neoforge.mixin;

import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RepairableItemsExtension.class)
public abstract class RepairableItemNeoForgeMixin implements IItemExtension {
    @Override
    public @NotNull ItemStack getCraftingRemainder(@NotNull ItemStack itemStack) {
        return RepairableItemsExtension.getCraftingRemainderCommon(itemStack);
    }
}
