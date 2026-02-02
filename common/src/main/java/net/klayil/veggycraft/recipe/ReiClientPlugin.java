package net.klayil.veggycraft.recipe;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import net.klayil.veggycraft.recipe.wait_recipe.ClockAnimatedEntry;
import net.klayil.veggycraft.recipe.wait_recipe.MetaDisplayPlus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.*;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ReiClientPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new MixedCategories(MixedCategories.PISTON_SMASH));
        registry.add(new MixedCategories(null, MolassesToBrownSugar.waitsValues));

        registry.addWorkstations(MixedCategories.PISTON_SMASH, EntryStacks.of(Blocks.PISTON));

        EntryStack<ItemStack> clockStack = EntryStacks.of(Items.CLOCK);
        clockStack.setting(EntryStack.Settings.RENDERER, entryStack -> new ClockAnimatedEntry());

        registry.addWorkstations(MixedCategories.HAVE_TO_WAIT, clockStack);
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
                return MixedCategories.HAVE_TO_WAIT;
            }
        });

        boolean isDataGen = System.getProperty("fabric-api.dataGen".toLowerCase()) != null;

        if (!isDataGen) {
            me.shedaniel.rei.api.common.entry.type.EntryDefinition<ItemStack> itemDef = me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes.ITEM.getDefinition();

            me.shedaniel.rei.api.common.entry.EntryStack<ItemStack> strippedBirchLogStack = me.shedaniel.rei.api.common.entry.EntryStack.of(itemDef, new ItemStack(Items.STRIPPED_BIRCH_LOG));
            me.shedaniel.rei.api.common.entry.EntryStack<ItemStack> evenStrippedBirchLogStack = me.shedaniel.rei.api.common.entry.EntryStack.of(itemDef, new ItemStack(ModItems.EVEN_STRIPPED_BIRCH_LOG.get()));

            registry.add(new me.shedaniel.rei.plugin.common.displays.DefaultStrippingDisplay(strippedBirchLogStack, evenStrippedBirchLogStack));

        }
    }
}
