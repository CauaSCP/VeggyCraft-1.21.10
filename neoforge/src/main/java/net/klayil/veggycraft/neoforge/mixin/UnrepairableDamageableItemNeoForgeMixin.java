package net.klayil.veggycraft.neoforge.mixin;

import net.klayil.veggycraft.item.UnrepairableDamageableItems;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(UnrepairableDamageableItems.class)
public abstract class UnrepairableDamageableItemNeoForgeMixin implements IItemExtension {
    @Override
    public @NotNull ItemStack getCraftingRemainder(@NotNull ItemStack itemStack) {
        return UnrepairableDamageableItems.getCraftingRemainderCommon(itemStack);
    }
}
