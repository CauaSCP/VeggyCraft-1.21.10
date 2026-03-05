package net.klayil.veggycraft.recipe;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.plugin.common.displays.brewing.DefaultBrewingDisplay;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import net.klayil.veggycraft.recipe.wait_recipe.MetaDisplayPlus;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.*;
import net.minecraft.world.level.block.Blocks;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MixedCategoriesRei(MixedCategoriesRei.PISTON_SMASH));
        registry.add(new MixedCategoriesRei(null, MolassesToBrownSugar.parsedTexts));

        registry.addWorkstations(MixedCategoriesRei.PISTON_SMASH, EntryStacks.of(Blocks.PISTON));

        registry.addWorkstations(MixedCategoriesRei.HAVE_TO_WAIT, EntryStacks.of(new ItemStack(ModItems.THIS_MOD_CLOCK)));
    }
    
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.add(new MetaDisplay(
                (ShapelessCraftingRecipeDisplay) new ShapelessRecipe("smash", CraftingBookCategory.REDSTONE, new ItemStack(ModItems.THIS_MOD_FLOUR), List.of(Ingredient.of(Items.WHEAT))).display().getFirst()
        ));

        registry.add(new MetaDisplayPlus(
                (ShapelessCraftingRecipeDisplay) new ShapelessRecipe("wait.Super.food", CraftingBookCategory.MISC, new ItemStack(ModItems.DRIED_MOLASSES), List.of(Ingredient.of(ModItems.MOLASSES_BOTTLE.get()))).display().getFirst()
        ) {
            @Override
            public CategoryIdentifier<?> getCategoryIdentifier() {
                return MixedCategoriesRei.HAVE_TO_WAIT;
            }
        });

        boolean isDataGen = System.getProperty("fabric-api.dataGen".toLowerCase()) != null;

        if (!isDataGen) {
            me.shedaniel.rei.api.common.entry.type.EntryDefinition<ItemStack> itemDef = me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes.ITEM.getDefinition();

            me.shedaniel.rei.api.common.entry.EntryStack<ItemStack> strippedBirchLogStack = me.shedaniel.rei.api.common.entry.EntryStack.of(itemDef, new ItemStack(Items.STRIPPED_BIRCH_LOG));
            me.shedaniel.rei.api.common.entry.EntryStack<ItemStack> evenStrippedBirchLogStack = me.shedaniel.rei.api.common.entry.EntryStack.of(itemDef, new ItemStack(ModItems.EVEN_STRIPPED_BIRCH_LOG.get()));

            registry.add(new me.shedaniel.rei.plugin.common.displays.DefaultStrippingDisplay(strippedBirchLogStack, evenStrippedBirchLogStack));

            ItemStack waterBottle = new ItemStack(Items.POTION);
            waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));

            VeggyCraft.myRecipesStacks.add(
                    Map.of(
                            new Item[] {
                                    ModItems.OTHER_SPLASH_POTION.get(),
                                    Items.SUGAR
                            },

                            ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS.getFirst()
                    )
            );

            VeggyCraft.myRecipesStacks.add(
                    Map.of(
                            new Item[] {
                                    ModItems.ALGAE_EXTRACT.get(),
                                    Items.KELP
                            },

                            waterBottle
                    )
            );

            for (Map<Item[], ItemStack> toGetKeys : Objects.requireNonNull(VeggyCraft.myRecipesStacks)) {
                VeggyCraft.LOGGER.warn("#SIZ: %s", VeggyCraft.myRecipesStacks.size());

                for (Item[] recipeStuff : toGetKeys.keySet()) {
                    DefaultBrewingDisplay recipe = new DefaultBrewingDisplay(
                            EntryIngredient.of(EntryStacks.of(toGetKeys.get(recipeStuff))),
                            EntryIngredient.of(EntryStacks.of(new ItemStack(recipeStuff[1]))),
                            EntryIngredient.of(EntryStacks.of(new ItemStack(recipeStuff[0])))
                    );

                    registry.add(recipe);
                }
            }
        }
    }
}
