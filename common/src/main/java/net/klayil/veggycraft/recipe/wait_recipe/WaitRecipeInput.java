package net.klayil.veggycraft.recipe.wait_recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record WaitRecipeInput(ItemStack input) implements RecipeInput {
    @Override
    public @NotNull ItemStack getItem(int i) {
        return input;
    }

    @Override
    public int size() {
        return 1;
    }
}
