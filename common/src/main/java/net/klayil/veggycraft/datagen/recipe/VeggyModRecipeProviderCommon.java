package net.klayil.veggycraft.datagen.recipe;

import net.klayil.veggycraft.VeggyCraft;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

//import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;

public class VeggyModRecipeProviderCommon extends RecipeProvider {
    public VeggyModRecipeProviderCommon(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new VeggyModRecipeProviderCommon(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

    public ShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
        HolderGetter<Item> itemLookup = this.registries.lookupOrThrow(Registries.ITEM);

        return ShapedRecipeBuilder.shaped(itemLookup, category, result);
    }

    protected ItemStack flourBagSize(int itemSizeAtBeginNum) {
//        final String itemName = "%02d_items_stacked_of_flour".formatted(itemSizeAtBeginNum);

        ItemStack to_return = new ItemStack(ModItems.FLOUR_BAG);

        DataComponentType<ModDataComponentTypes.ItemHealth> HEALTH =
                ModDataComponentTypes.HEALTH.get();

        ModDataComponentTypes.ItemHealth old =
                to_return.getOrDefault(HEALTH, new ModDataComponentTypes.ItemHealth(0, itemSizeAtBeginNum));


        to_return.set(
                HEALTH,
                new ModDataComponentTypes.ItemHealth(itemSizeAtBeginNum, old.max())
        );

        return to_return;
    }

    public ShapedRecipeBuilder flourRecipe(int resultItemSize) {
        Item flour = ModItems.THIS_MOD_FLOUR.get();
        ItemStack result = flourBagSize(resultItemSize);

        ShapedRecipeBuilder ret = shaped(RecipeCategory.MISC, result);

        ret = ret
                .pattern("fff")
                .pattern("fBf")
                .pattern("fff")
                .define('f', flour)
                .define('B', ComponentIngredientOf(flourBagSize(resultItemSize - 8)));

        return ret;
    }

    @Override
    public void buildRecipes() {

        buildRecipesCommon();

    }

    protected Ingredient ComponentIngredientOf(ItemStack ingredientStack) {
        return Ingredient.of(ingredientStack.getItem());
    }


//    protected ItemStack[] ingredientItemStacksWithHealth = new ItemStack[3];
//    protected net.klayil.veggycraft.datagen.recipe.ShapedRecipeBuilder[] rawSeitanWithHealthRecipes =
//            new net.klayil.veggycraft.datagen.recipe.ShapedRecipeBuilder[2];


    public void buildRecipesCommon() {
        ItemStack bag;

        @NotNull
        Ingredient ing = Ingredient.of(Items.WHEAT);
//        Ingredient ing = Ingredient.of(Items.WHEAT.asItem(), Blocks.PISTON);

        PistonSmashRecipeBuilder
            .smash(ing, new ItemStack(ModItems.THIS_MOD_FLOUR))
            .unlockedBy("has_wheat", has(Items.WHEAT))
            .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_smash")));

//        PistonOverWorldRecipeBuilder
//                .smash(ing, new ItemStack(ModItems.THIS_MOD_FLOUR.get()))
//                .unlockedBy("has_wheat", has(Items.WHEAT))
//                // Make sure this method actually passes the Serializer to the RecipeOutput
//                .save(output, String.valueOf(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_smash")));

        for (int size = 8; size <= 64; size+=8) {
            bag = flourBagSize(size);

            shapeless(RecipeCategory.MISC, new ItemStack(ModItems.THIS_MOD_FLOUR, 8))
                    .requires(ComponentIngredientOf(bag))
                    .unlockedBy("has_bag_of_%d_flours".formatted(size), has(bag.getItem()))
                    .save(
                            output,
                            "%s:wheat_flour_from_bag_of_%d".formatted(VeggyCraft.MOD_ID, size)
                    );

            if (size < 16) continue;

            flourRecipe(size).unlockedBy("has_before_%d_bag".formatted(size), has(ModItems.THIS_MOD_FLOUR.get())).
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

        shapeless(RecipeCategory.MISC, ModItems.THIS_MOD_FLOUR.get())
                .requires(Items.PISTON)
                .requires(Items.WHEAT)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(output);

        ItemStack drySeitanHealthFirstHealth = null;

        Ingredient ingredient = null;
        for (int health = 0; true; health++) {
            ItemStack drySeitanWithHealth = new ItemStack(ModItems.DRY_RAW_SEITAN_0);
            ItemStack ingredientStack = drySeitanWithHealth.copy();
            ingredientStack.set(
                    ModDataComponentTypes.HEALTH.get(),
                    new ModDataComponentTypes.ItemHealth(health, 3)
            );
            ingredient = ComponentIngredientOf(ingredientStack);

            if (health == 0) {
                drySeitanHealthFirstHealth = ingredientStack;
            }

            if (health > 2) break;

            drySeitanWithHealth.set(
                    ModDataComponentTypes.HEALTH.get(),
                    new ModDataComponentTypes.ItemHealth(health+1, 3)
            );

            shaped(RecipeCategory.MISC, drySeitanWithHealth)
                    .pattern("SB")
                    .define('B', Items.WATER_BUCKET)
                    .unlockedBy("has_water", has(Items.WATER_BUCKET))
                    .define('S', ingredient)
                    .save(output, "%s:raw_seitan_health_%d".formatted(VeggyCraft.MOD_ID, health+1));
        }

        shaped(RecipeCategory.MISC, drySeitanHealthFirstHealth).
                pattern("FB")
                .define('B', Items.WATER_BUCKET)
                .define('F', ModItems.THIS_MOD_FLOUR.get())
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(
                        output
                        //, "%s:raw_seitan_from_flour".formatted(VeggyCraft.MOD_ID)
                );

        assert ingredient != null;
        shaped(RecipeCategory.MISC, ModItems.WET_RAW_SEITAN.get())
                .pattern("SB")
                .define('S', ingredient)
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(output);

        cookingRecipeFood(new ItemStack(ModItems.WET_RAW_SEITAN), ModItems.SEITAN_COOKED_BEEF.get(), 0.35f, 200);
    }

    private void cookingRecipeFood(ItemStack ingredientStack, ItemLike result, float experience, int cookingTime) {
        String hasIngredientText = "has_%s".formatted(
                BuiltInRegistries.ITEM.getKey(ingredientStack.getItem()).getPath()
        );

        String resultName = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();

        SimpleCookingRecipeBuilder.smelting(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_furnace".formatted(resultName));

        SimpleCookingRecipeBuilder.smoking(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime / 2)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_smoker".formatted(resultName));

        SimpleCookingRecipeBuilder.campfireCooking(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime * 3)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_campfire".formatted(resultName));
    }
}
