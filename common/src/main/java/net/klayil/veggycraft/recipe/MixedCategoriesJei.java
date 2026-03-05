package net.klayil.veggycraft.recipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
//import net.klayil.veggycraft.recipe.wait_recipe.CustomClockRendererJei;
import net.klayil.veggycraft.recipe.wait_recipe.WaitRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MixedCategoriesJei<T extends Recipe<? extends RecipeInput>> implements IRecipeCategory<T> {
    private static final ResourceLocation SMASH_UID = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "piston_smashin");
    private static final ResourceLocation WAIT_UID = ResourceLocation.fromNamespaceAndPath(
            VeggyCraft.MOD_ID,
            "do_wait"
    );

    public static final IRecipeType<WaitRecipe> WAIT_TYPE_RECIPE = IRecipeType.create(WAIT_UID, WaitRecipe.class);
    public static final IRecipeType<PistonSmashRecipe> PISTON_SMASH_TYPE = IRecipeType.create(SMASH_UID, PistonSmashRecipe.class);
    public final IRecipeType<T> recipeType;

    public static final ResourceLocation TEXTURE = PistonSmashRecipe.TEXTURE;

    public final IDrawable background;

    public final IDrawable horBorder;
    public final IDrawable vertBorder;

    public IDrawable icon;

    public final IDrawable slot;

    private final int inputSlotPosition = 33;
    private final int outputSlotPosition = inputSlotPosition + 64;
    private final int slotVerticalPosition = 33;


    final static AtomicReference<List<MutableComponent>> timeTexts = new AtomicReference<>(null);

    public ItemStack masterIcon;

//    final private Minecraft minecraftInstance;


    @SafeVarargs
    public MixedCategoriesJei(IRecipeType<T> recipeTypeParam, IGuiHelper helper, List<MutableComponent>... _parsedTimeTexts) {
        recipeType = recipeTypeParam;

        background = helper.createDrawable(TEXTURE, 31, 3, 140, 80);

        if (recipeType == PISTON_SMASH_TYPE) {
            masterIcon = new ItemStack(Blocks.PISTON);
            icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, masterIcon);
        }
        else if (recipeType == WAIT_TYPE_RECIPE) {
            masterIcon = new ItemStack(ModItems.THIS_MOD_CLOCK);
            icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, masterIcon);

            timeTexts.set(Arrays.stream(_parsedTimeTexts).findFirst().orElse(null));
        }
        else
            throw new IllegalArgumentException("identified recipe type of /%s:%s\\ does not exist".formatted(recipeType.getUid().getNamespace(), recipeType.getUid().getPath()));

        slot = helper.getSlotDrawable();

        horBorder = helper.createDrawable(TEXTURE, 8, 70, 160, 8);
        vertBorder = helper.createDrawable(TEXTURE, 146, 3, 26, 80);

//        this.minecraftInstance = Minecraft.getInstance();
    }

    @Override
    public @NotNull IRecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        if (recipeType == PISTON_SMASH_TYPE)
            return Component.translatable("klay_api.smash.predicate").append(Component.translatable("block.minecraft.piston")).append(Component.translatable("klay_api.smash.wan"));
        else if (recipeType == WAIT_TYPE_RECIPE)
            return Component.translatable("klay_api.have.to.wait");

        throw new IllegalArgumentException("identified recipe type of /%s:%s\\ does not exist".formatted(recipeType.getUid().getNamespace(), recipeType.getUid().getPath()));
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

// /* *
//    @Override
//    public void getTooltip(ITooltipBuilder tooltip, T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
//        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
//
//        Map<ResourceLocation, Component> hoverCases = ((ItemMixinImpl) masterIcon.getItem()).get$HoverCases();
//        ResourceLocation iconItemID = BuiltInRegistries.ITEM.getKey(masterIcon.getItem());
//
//        if (!hoverCases.containsKey(iconItemID)) return;
//
// //       VeggyCraft.LOGGER.info("#Mouse: {%s, %s}", mouseX, mouseY);
//
//        Component comp = hoverCases.get(iconItemID);
//
//        tooltip.add(comp);
//    }
// */

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        if (recipe instanceof PistonSmashRecipe pistonSmashRecipe) {
            builder.addInputSlot(inputSlotPosition, slotVerticalPosition).add(pistonSmashRecipe.getIngredients().getFirst());
            builder.addOutputSlot(outputSlotPosition, slotVerticalPosition).add(pistonSmashRecipe.getResultItem(null));
        } else if (recipe instanceof WaitRecipe waitRecipe) {
            builder.addInputSlot(inputSlotPosition, slotVerticalPosition).add(waitRecipe.getIngredients().getFirst());
            builder.addOutputSlot(outputSlotPosition, slotVerticalPosition).add(waitRecipe.getResultItem(null));
        }
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
    public void draw(@NotNull T recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        background.draw(guiGraphics, 3, 2); // 4, 2

        int slotBackgroundWidth = slot.getWidth() + 2;
        int coverRightPartCraftPos = inputSlotPosition + 4 - (slotBackgroundWidth / 2) - ((slotBackgroundWidth - vertBorder.getWidth()) / 2);

        int outputCoverBackground = outputSlotPosition + 4 - 1 + (slot.getWidth() - vertBorder.getWidth());
        vertBorder.draw(guiGraphics, outputCoverBackground - 1, slotVerticalPosition - 37);
        vertBorder.draw(guiGraphics, outputCoverBackground + 34, slotVerticalPosition - 37);

        horBorder.draw(guiGraphics, coverRightPartCraftPos - 35, slotVerticalPosition - 37);

        vertBorder.draw(guiGraphics, coverRightPartCraftPos - 35, slotVerticalPosition - 37);
        vertBorder.draw(guiGraphics, coverRightPartCraftPos - 35 + vertBorder.getWidth(), slotVerticalPosition - 37);
        vertBorder.draw(guiGraphics, coverRightPartCraftPos, slotVerticalPosition - 37);

        horBorder.draw(guiGraphics, coverRightPartCraftPos - 35, slotVerticalPosition - 37 + vertBorder.getHeight());

        slot.draw(guiGraphics, inputSlotPosition - 1, slotVerticalPosition - 1);
        slot.draw(guiGraphics, outputSlotPosition - 1, slotVerticalPosition - 1);

        if (this.getRecipeType() == WAIT_TYPE_RECIPE) {
            int enMoreVert = 0;

            for (Component _text : timeTexts.get()) {
                MutableComponent text = _text.copy();

                guiGraphics.drawCenteredString(
                    Minecraft.getInstance().font,
                    text,
                    outputSlotPosition - inputSlotPosition + ((slot.getWidth() + 2) / 2),
                    slotVerticalPosition + slot.getHeight() + 4 + 2 + enMoreVert, 0xFFFFFFFF
                );

                enMoreVert += 10;
            }
        }
    }
}