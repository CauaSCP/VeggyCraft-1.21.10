package net.klayil.veggycraft.fabric.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.datagen.recipe.VeggyModRecipeProviderCommon;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import java.util.concurrent.CompletableFuture;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;

public class VeggyModRecipeProviderFabric extends VeggyModRecipeProviderCommon {
    public VeggyModRecipeProviderFabric(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new VeggyModRecipeProviderFabric(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

//
//    public net.klayil.veggycraft.fabric.datagen.ShapedRecipeBuilderFabric shaped(RecipeCategory category, ItemStack result) {
//        HolderGetter<Item> itemLookup = this.registries.lookupOrThrow(Registries.ITEM);
//
//        return net.klayil.veggycraft.fabric.datagen.ShapedRecipeBuilderFabric.shaped(itemLookup, category, result);
//    }

    @SuppressWarnings("unchecked")
    private static <T> DataComponentPatch.Builder setComponent(
            DataComponentPatch.Builder builder,
            DataComponentType<T> type,
            Object value
    ) {
        return builder.set(type, (T) value);
    }


    @Override
    protected Ingredient ComponentIngredientOf(ItemStack ingredientStack) {
        return FabricComponentIngredientOf(ingredientStack);
    }
    private Ingredient FabricComponentIngredientOf(ItemStack ingredientStack) {
        DataComponentMap componentsMap = ingredientStack.getComponents();

        if (componentsMap == DataComponentMap.EMPTY) VeggyCraft.LOGGER.warn("#WARN: Ingredient Stack components for %s is EMPTY".formatted(
                ingredientStack.getItem().toString()
        ));

        return DefaultCustomIngredients.components(
                Ingredient.of(ingredientStack.getItem()),
                builder -> {
                    componentsMap.stream()
                            .forEach(e -> setComponent(builder, e.type(), e.value()));
                    return builder;
                }
        );
    }


    @Override
    public void buildRecipes() {

        buildRecipesCommon();

    }
}
