package net.klayil.veggycraft.datagen.recipe;

import net.klayil.veggycraft.recipe.PistonSmashRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class PistonSmashRecipeBuilder implements RecipeBuilder {
    private final Ingredient input;
    private final ItemStack result;
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    @Nullable
    private String group;

    private PistonSmashRecipeBuilder(Ingredient input, ItemStack result) {
        this.input = input;
        this.result = result;
    }

    public static PistonSmashRecipeBuilder smash(Ingredient input, ItemStack result) {
        return new PistonSmashRecipeBuilder(input, result);
    }

    // Helper for simple item inputs
    public static PistonSmashRecipeBuilder smash(Item input, Item result, int count) {
        return new PistonSmashRecipeBuilder(Ingredient.of(input), new ItemStack(result, count));
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.criteria.put(name, criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        resKeyId = resourceKey;

        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resKeyId))
                .rewards(AdvancementRewards.Builder.recipe(resKeyId))
                .requirements(AdvancementRequirements.Strategy.OR);

        this.criteria.forEach(advancementBuilder::addCriterion);

        // Create the actual recipe object
        PistonSmashRecipe recipe = new PistonSmashRecipe(this.input, this.result);

        // Save it to the data generator
        output.accept(resKeyId, recipe, advancementBuilder.build(resKeyId.location().withPrefix("recipes/")));
    }

    public ResourceKey<Recipe<?>> resKeyId;
//    public void saveLoc(RecipeOutput output, ResourceLocation loc) {
//        resKeyId = ResourceKey.create(Registries.RECIPE, loc);
//
//        Advancement.Builder advancementBuilder = output.advancement()
//                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resKeyId))
//                .rewards(AdvancementRewards.Builder.recipe(resKeyId))
//                .requirements(AdvancementRequirements.Strategy.OR);
//
//        this.criteria.forEach(advancementBuilder::addCriterion);
//
//        // Create the actual recipe object
//        PistonSmashRecipe recipe = new PistonSmashRecipe(this.input, this.result);
//
//        // Save it to the data generator
////        output.accept(resKeyId, recipe, advancementBuilder.build(resKeyId.location().withPrefix("recipes/")));
//
//        save(output, resKeyId);
//    }

//    @Override
//    public void save(RecipeOutput recipeOutput, String id) {
//        // Build the advancement that unlocks the recipe
//
//    }
}