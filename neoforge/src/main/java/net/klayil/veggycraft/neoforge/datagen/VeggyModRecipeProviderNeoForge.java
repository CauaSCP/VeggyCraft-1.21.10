package net.klayil.veggycraft.neoforge.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import net.klayil.veggycraft.datagen.recipe.VeggyModRecipeProviderCommon;


public class VeggyModRecipeProviderNeoForge extends VeggyModRecipeProviderCommon {
    public VeggyModRecipeProviderNeoForge(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new VeggyModRecipeProviderNeoForge(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

    @Override
    protected Ingredient ComponentIngredientOf(ItemStack ingredientStack) {
        return DataComponentIngredient.of(false, ingredientStack);
    }

    @Override
    public void buildRecipes() {

        buildRecipesCommon();

    }
}
