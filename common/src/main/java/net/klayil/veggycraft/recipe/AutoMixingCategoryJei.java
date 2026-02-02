package net.klayil.veggycraft.recipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AutoMixingCategoryJei implements IRecipeCategory<PistonSmashRecipe> {
    public static final ResourceLocation SMASH_UID = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "piston_smashin");
    public static final IRecipeType<PistonSmashRecipe> PISTON_SMASH_TYPE = IRecipeType.create(SMASH_UID, PistonSmashRecipe.class);

    public static final ResourceLocation TEXTURE = PistonSmashRecipe.TEXTURE;

    public final IDrawable background;
    public final IDrawable background_plus;
    public final IDrawable icon;

    public final IDrawable slot;

    private final int inputSlotPosition = 54-6-((14)/2);
    private final int outputSlotPosition = 54-6+64-((14)/2);
    private final int slotVerticalPosition = 33;


    public AutoMixingCategoryJei(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4+14+7, 3, 169-4-14-7, 80);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Blocks.PISTON));

        this.slot = helper.getSlotDrawable();
        this.background_plus = helper.createDrawable(TEXTURE, 146, 30, 26, 26);
    }

    @Override
    public @NotNull IRecipeType<PistonSmashRecipe> getRecipeType() {
        return PISTON_SMASH_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("klay_api.smash.predicate").append(Component.translatable("block.minecraft.piston")).append(Component.translatable("klay_api.smash.wan"));
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public int getWidth() {
        return background.getWidth()+8;
    }

    @Override
    public void draw(@NotNull PistonSmashRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        background.draw(guiGraphics, 4, 2);

        background_plus.draw(guiGraphics, 119-14-7, 29);

        int[] xs = {28-14-7, 44-14-7, 64-14-7};
        for (int x : xs) {
            background_plus.draw(guiGraphics, x, 15);
            background_plus.draw(guiGraphics, x, 25);
            background_plus.draw(guiGraphics, x, 43);
        }

        slot.draw(guiGraphics, inputSlotPosition - 1, slotVerticalPosition - 1);
        slot.draw(guiGraphics, outputSlotPosition - 1, slotVerticalPosition - 1);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PistonSmashRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(inputSlotPosition, slotVerticalPosition).add(recipe.getIngredients().getFirst());
        builder.addOutputSlot(outputSlotPosition, slotVerticalPosition).add(recipe.getResultItem(null));
    }
}
