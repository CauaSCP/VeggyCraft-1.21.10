package net.klayil.veggycraft.mixin;

import lombok.SneakyThrows;
import mezz.jei.api.gui.drawable.IScalableDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.common.util.ImmutablePoint2i;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.library.gui.ingredients.CycleTicker;
import mezz.jei.library.gui.recipes.RecipeLayout;
import mezz.jei.library.gui.recipes.ShapelessIcon;
import net.klayil.SequencedList;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.JeiIcon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@Mixin(value = RecipeLayout.class, remap = false)
public class RecipeLayoutMixin<RC extends IRecipeCategory<?>, R> {

//    @Shadow(remap = false) @Final private RC recipeCategory;
//    @Shadow(remap = false) @Final private List<IRecipeSlotDrawable> slots;
//    @Shadow(remap = false) @Final private R recipe;
//
//    @Shadow(remap = false) private ImmutableRect2i area;

//    @Shadow(remap = false) private ShapelessIcon shapelessIcon;

    @Shadow @Final private IRecipeCategory<R> recipeCategory;

}
