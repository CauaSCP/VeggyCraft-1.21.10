package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

//import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;

public class VeggyModRecipeProvider extends net.minecraft.data.recipes.RecipeProvider {
    public VeggyModRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.@NotNull Provider provider, @NotNull RecipeOutput recipeOutput) {
            return new VeggyModRecipeProvider(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
        HolderGetter<Item> itemLookup = this.registries.lookupOrThrow(Registries.ITEM);

        return ShapedRecipeBuilder.shaped(itemLookup, category, result);
    }

    protected Item flourBagSize(int itemSizeAtBeginNum) {
        final String itemName = "%02d_items_stacked_of_flour".formatted(itemSizeAtBeginNum);

        return BuiltInRegistries.ITEM.getValue(
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemName)
        );
    }

    public ShapedRecipeBuilder flourRecipe(int resultItemSize) {
        Item flour = ModItems.THIS_MOD_FLOUR.get();
        ItemStack result = new ItemStack(flourBagSize(resultItemSize));

        result.set(
                ModDataComponentTypes.HEALTH,
                new ModDataComponentTypes.ItemHealth(resultItemSize, Objects.requireNonNull(result.get(ModDataComponentTypes.HEALTH)).max())
        );

        ShapedRecipeBuilder ret = shaped(RecipeCategory.MISC, result);

        ret = ret
                .pattern("fff") // Caused by: java.lang.NullPointerException: Cannot invoke "net.minecraft.data.recipes.ShapedRecipeBuilder.pattern(String)" because "ret" is null
                .pattern("fBf")
                .pattern("fff")
                .define('f', flour)
                .define('B', flourBagSize(resultItemSize - 8));

        return ret;
    }

    @Override
    protected void buildRecipes() {

        buildRecipesCommon();

    }

    public void buildRecipesCommon() {
//        if (!Platform.isForgeLike()) return;

        Item bag;

        for (int size = 8; size <= 64; size+=8) {
            bag = flourBagSize(size);

            shapeless(RecipeCategory.MISC, new ItemStack(ModItems.THIS_MOD_FLOUR, 8))
                    .requires(bag)
                    .unlockedBy("has_bag_of_%d_flours".formatted(size), has(bag))
                    .save(
                            output,
                            "%s:wheat_flour_from_bag_of_%d".formatted(VeggyCraft.MOD_ID, size)
                    );

            if (size < 16) continue;

            flourRecipe(size).unlockedBy("has_before_%d_bag".formatted(size), has(flourBagSize(size-8))).
                    save(
                            output,
                            "%s:bag_of_%d_crafting_and_advt".formatted(VeggyCraft.MOD_ID, size)
                    );
        }

        shapeless(RecipeCategory.MISC, ModItems.BLACK_DYE_STACK)
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.COAL_CARBON_CUTTER.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(
                        output,
                        "%s:carbon_dye_coal_cut".formatted(VeggyCraft.MOD_ID)
                );

        ItemStack threeBlackDye = ModItems.BLACK_DYE_STACK.copy();
        threeBlackDye.setCount(3);
        shapeless(RecipeCategory.MISC, threeBlackDye)
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.DIAMOND_CARBON_CUTTER.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(
                        output,
                        "%s:carbon_dye_diamon_cut".formatted(VeggyCraft.MOD_ID)
                );

        shaped(RecipeCategory.MISC, new ItemStack(ModItems.BLACK_OF_COAL_CARBON))
                .pattern("##")
                .pattern("##")
                .define('#', ItemTags.COALS)
                //.unlockedBy("has_normal_coal", has(Items.COAL))
                .unlockedBy("has_coal_tag", has(ItemTags.COALS))
                //.unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output);

        shapeless(RecipeCategory.MISC, new ItemStack(ModItems.SHINY_OF_DIAMOND_COAL_CARBON))
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get()),
                Ingredient.of(ModItems.COAL_CARBON_CUTTER.get()),
                Ingredient.of(Items.COAL),
                RecipeCategory.TOOLS,
                ModItems.DIAMOND_CARBON_CUTTER.get()
        ).unlocks(
                "has_coal_carbon_cutter",
                has(ModItems.COAL_CARBON_CUTTER.get())
        ).save(output, "%s:carbon_cutter_coal_to_diamond".formatted(VeggyCraft.MOD_ID));

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get()),
                Ingredient.of(ModItems.COAL_CARBON_CUTTER.get()),
                Ingredient.of(Items.CHARCOAL),
                RecipeCategory.TOOLS,
                ModItems.DIAMOND_CARBON_CUTTER.get()
        ).unlocks(
                "has_coal_carbon_cutter",
                has(ModItems.COAL_CARBON_CUTTER.get())
        ).save(output, "%s:carbon_cutter_coal_to_diamond_charcoal_addition".formatted(VeggyCraft.MOD_ID));

        shaped(RecipeCategory.MISC, new ItemStack(ModItems.COAL_CARBON_CUTTER))
                .pattern("CBC")
                .pattern(" | ")
                .pattern(" | ")
                .define('C', ItemTags.COALS)
                .define('B', ModItems.BLACK_OF_COAL_CARBON.get())
                .define('|', Items.STICK)
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(output);

        ItemStack drySeitanWithHealth = new ItemStack(ModItems.DRY_RAW_SEITAN_1);
        drySeitanWithHealth.set(
                ModDataComponentTypes.HEALTH,
                new ModDataComponentTypes.ItemHealth(1, 3)
        );

        shaped(RecipeCategory.MISC, drySeitanWithHealth)
                .pattern("SB")
                .define('S', ModItems.DRY_RAW_SEITAN_0.get())
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(output);

        drySeitanWithHealth = new ItemStack(ModItems.DRY_RAW_SEITAN_2);
        drySeitanWithHealth.set(
                ModDataComponentTypes.HEALTH,
                new ModDataComponentTypes.ItemHealth(2, 3)
        );

        shaped(RecipeCategory.MISC, drySeitanWithHealth)
                .pattern("SB")
                .define('S', ModItems.DRY_RAW_SEITAN_1.get())
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(output);

        shaped(RecipeCategory.MISC, ModItems.WET_RAW_SEITAN.get())
                .pattern("SB")
                .define('S', ModItems.DRY_RAW_SEITAN_2.get())
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(output);
    }
}
