package net.klayil.veggycraft.recipe.wait_recipe;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.klayil.veggycraft.recipe.MetaDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;

import java.util.List;

abstract public class MetaDisplayPlus extends MetaDisplay {
    abstract public CategoryIdentifier<?> getCategoryIdentifier();

    public MetaDisplayPlus(List<EntryIngredient> list, List<EntryIngredient> es) {
        super(list, es);
    }

    public MetaDisplayPlus(ShapelessCraftingRecipeDisplay recipe) {
        super(recipe);
    }
}
