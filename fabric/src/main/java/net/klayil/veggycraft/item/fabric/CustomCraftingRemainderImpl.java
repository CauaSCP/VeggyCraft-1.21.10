package net.klayil.veggycraft.item.fabric;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.CustomCraftingRemainder;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface CustomCraftingRemainderImpl extends FabricItem {
    @Override
    default @NotNull ItemStack getRecipeRemainder(@NotNull ItemStack stack) {
        VeggyCraft.LOGGER.warn("#Remainder getting");

        return ((CustomCraftingRemainder) this).getChangedCraftingRemainder(stack);
    }

    default @NotNull ItemStack getCraftingRemainder(@NotNull ItemStack stack) {
        return getRecipeRemainder(stack);
    }
}