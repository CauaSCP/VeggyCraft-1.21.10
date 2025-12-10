package net.klayil.veggycraft.datagen.recipe;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;

import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class VeggyModRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public VeggyModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new VeggyModRecipeProvider(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

    private Item flourBagSize(int itemSizeAtBeginNum) {
        return BuiltInRegistries.ITEM.getValue(
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "%02d_items_stacked_of_flour".formatted(itemSizeAtBeginNum))
        );
    }

    ShapelessRecipeBuilder flourRecipe(int resultItemSize) {
        ItemStack result = new ItemStack(flourBagSize(resultItemSize));
        result.setDamageValue(resultItemSize);

        return shapeless(RecipeCategory.MISC, result).requires(flourBagSize(resultItemSize + 8));
    }

    @Override
    protected void buildRecipes() {
        for (int size = 12; size < 64; size+=8) {
            flourRecipe(size).save(output);
        }
    }
}
