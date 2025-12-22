package net.klayil.veggycraft.datagen.recipe;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.NotNull;

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
        final String itemName = "%02d_items_stacked_of_flour".formatted(itemSizeAtBeginNum);

        return BuiltInRegistries.ITEM.getValue(
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemName)
        );
    }

    ShapelessRecipeBuilder flourRecipe(int resultItemSize) {
        Item flour = ModItems.THIS_MOD_FLOUR.get();
        ItemStack result = new ItemStack(flourBagSize(resultItemSize));

        result.setDamageValue(64 - resultItemSize + 1);

        ShapelessRecipeBuilder ret = shapeless(RecipeCategory.MISC, result);

        for (int a_i = 0; a_i < 4; a_i++) ret = ret.requires(flour);

        ret = ret.requires(flourBagSize(resultItemSize - 8));

        for (int b_i = 0; b_i < 4; b_i++) ret = ret.requires(flour);

        return ret;
    }

    @Override
    protected void buildRecipes() {
        for (int size = 16; size <= 64; size+=8) {
            flourRecipe(size).unlockedBy("has_before_%d_bag".formatted(size), has(flourBagSize(size-8))).
                save(
                    output,
                    "%s:bag_of_%d_shapeless_crafting_and_advt".formatted(VeggyCraft.MOD_ID, size)
                );
        }
    }
}
