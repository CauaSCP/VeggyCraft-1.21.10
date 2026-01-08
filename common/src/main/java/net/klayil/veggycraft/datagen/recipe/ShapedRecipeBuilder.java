package net.klayil.veggycraft.datagen.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.data.recipes.RecipeOutput;
import org.jetbrains.annotations.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;

public class ShapedRecipeBuilder implements RecipeBuilder {
    private final HolderGetter<Item> items;
    private final RecipeCategory category;
    private final Item result;
    private final int count;
    private final ItemStack resultStack;
    private final List<String> rows = Lists.<String>newArrayList();
    private final Map<Character, Ingredient> key = Maps.<Character, Ingredient>newLinkedHashMap();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap();
    @Nullable
    private String group;
    private boolean showNotification = true;

    private ShapedRecipeBuilder(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        this(items, category, new ItemStack(result, count));
    }

    private ShapedRecipeBuilder(HolderGetter<Item> arg, RecipeCategory arg2, ItemStack result) {
        this.items = arg;
        this.category = arg2;
        this.result = result.getItem();
        this.count = result.getCount();
        this.resultStack = result;
    }

    public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike result) {
        return shaped(items, category, result, 1);
    }

    public static ShapedRecipeBuilder shaped(HolderGetter<Item> items, RecipeCategory category, ItemLike result, int count) {
        return new ShapedRecipeBuilder(items, category, result, count);
    }

    public static ShapedRecipeBuilder shaped(HolderGetter<Item> arg, RecipeCategory arg2, ItemStack result) {
        return new ShapedRecipeBuilder(arg, arg2, result);
    }

    /**
     * Adds a key to the recipe pattern.
     */
    public ShapedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(this.items.getOrThrow(tag)));
    }

    /**
     * Adds a key to the recipe pattern.
     */
    public ShapedRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    /**
     * Adds a key to the recipe pattern.
     */
    public ShapedRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    /**
     * Adds a new entry to the patterns for this recipe.
     */
    public ShapedRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != ((String)this.rows.get(0)).length()) {
            throw new IllegalArgumentException("Pattern must be the same width on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public ShapedRecipeBuilder unlockedBy(String string, Criterion<?> arg) {
        this.criteria.put(string, arg);
        return this;
    }

    public ShapedRecipeBuilder group(@Nullable String string) {
        this.group = string;
        return this;
    }

    public ShapedRecipeBuilder showNotification(boolean showNotification) {
        this.showNotification = showNotification;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        ShapedRecipePattern shapedrecipepattern = this.ensureValid(resourceKey);
        Advancement.Builder advancement$builder = output.advancement()
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.criteria.forEach(advancement$builder::addCriterion);
        ShapedRecipe shapedrecipe = new ShapedRecipe(
                (String)Objects.requireNonNullElse(this.group, ""),
                RecipeBuilder.determineBookCategory(this.category),
                shapedrecipepattern,
                this.resultStack,
                this.showNotification
        );
        output.accept(resourceKey, shapedrecipe, advancement$builder.build(resourceKey.location().withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private ShapedRecipePattern ensureValid(ResourceKey<Recipe<?>> recipe) {
        if (this.criteria.isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + recipe.location());
        } else {
            return ShapedRecipePattern.of(this.key, this.rows);
        }
    }
}