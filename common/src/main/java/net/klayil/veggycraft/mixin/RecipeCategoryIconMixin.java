package net.klayil.veggycraft.mixin;

import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.gui.recipes.RecipeCategoryTab;
import net.klayil.SequencedList;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.MixedCategoriesJei;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(value = RecipeCategoryTab.class, remap = false)
public class RecipeCategoryIconMixin<RC extends IRecipeCategory<?>> {
    @Unique private final Minecraft game$instance = Minecraft.getInstance();

    @Shadow @Final private RC category;

    @Inject(method = "getTooltip", at = @At("HEAD"), cancellable = true)
    private void changeIcon(CallbackInfoReturnable<JeiTooltip> cir) {
//        @Nullable ItemStack masterIconNullable = null;

//        Field[] contentingItemsArray = category.getClass().getFields();

//        for (int i = 0; i < contentingItemsArray.length; i++) {
//            Field contentValue = contentingItemsArray[i];
//            Object parsedContent;
//
//            if (contentValue.getName().equals("masterIcon")) {
//                parsedContent = contentValue.get(category);
//
//                if (parsedContent instanceof ItemStack _masterIconNullable) {
//                    masterIconNullable = _masterIconNullable;
//                    break;
//                }
//                else if
//                (category.getClass().getSuperclass().getPackageName()
//                                .toLowerCase()
//                                .startsWith("net.klayil.veggycraft."))
//                {
//                    throw new SequencedList.IncompatibleTypeError(
//                            "Error: " +
//                                    "veggycraft package JeiRecipeCategories may" +
//                                    "not have a stack that isn't ItemStack"
//                    );
//                }
//            } else if (i + i == contentingItemsArray.length) {
//                return;
//            }
//        }

        if (!(category instanceof MixedCategoriesJei<?> categoryWithMasterIcon)) return;

//        assert masterIconNullable != null;
        final ItemStack masterIcon = categoryWithMasterIcon.masterIcon;

        JeiTooltip tooltip = new JeiTooltip();
        Component title = category.getTitle();

        //noinspection ConstantConditions
        if (title != null) {
            tooltip.add(title);
        }

        masterIcon.getTooltipLines(Item.TooltipContext.of(game$instance.level), game$instance.player, TooltipFlag.NORMAL)
                .forEach(component -> {
                    if (component.getStyle().getColor() != TextColor.fromLegacyFormat(ChatFormatting.WHITE))
                        tooltip.add(component);
                });

        ResourceLocation uid = category.getRecipeType().getUid();
        String modId = uid.getNamespace();
        IModIdHelper modIdHelper = Internal.getJeiRuntime().getJeiHelpers().getModIdHelper();
        if (modIdHelper.isDisplayingModNameEnabled()) {
            String modName = modIdHelper.getFormattedModNameForModId(modId);
            tooltip.add(Component.literal(modName));
        }

        cir.setReturnValue(tooltip);
    }
}
