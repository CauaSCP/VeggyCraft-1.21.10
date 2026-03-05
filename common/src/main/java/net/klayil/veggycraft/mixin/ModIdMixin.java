package net.klayil.veggycraft.mixin;

import com.mojang.datafixers.util.Either;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.JeiTooltip;
import mezz.jei.common.platform.IPlatformRenderHelper;
import mezz.jei.common.platform.Services;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(value = JeiTooltip.class, remap = false)
public abstract class ModIdMixin {
    @Shadow public abstract void add(@Nullable FormattedText formattedText);

    @Shadow @Final private List<Either<FormattedText, TooltipComponent>> lines;

    @Inject(
            method = "draw(Lnet/minecraft/client/gui/GuiGraphics;IILmezz/jei/api/ingredients/ITypedIngredient;Lmezz/jei/api/ingredients/IIngredientRenderer;Lmezz/jei/api/runtime/IIngredientManager;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private <T> void getFormattedModNameForModId(GuiGraphics guiGraphics, int x, int y, ITypedIngredient<T> typedIngredient, IIngredientRenderer<T> ingredientRenderer, IIngredientManager ingredientManager, CallbackInfo ci) {
        Optional<ItemStack> stack = typedIngredient.getItemStack();
        final ItemStack thisModClockStack = new ItemStack(ModItems.THIS_MOD_CLOCK);

        /* *
        if (stack.isPresent() && "minecraft".equals(BuiltInRegistries.ITEM.getKey(stack.get().getItem()).getNamespace())) {
            IJeiHelpers jeiHelpers = Internal.getJeiRuntime().getJeiHelpers();
            IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
            modIdHelper.getModNameForTooltip(typedIngredient).ifPresent(component -> {
                String whereFrom = "%s | literal".formatted(component.getString());

                if (component.getContents() instanceof TranslatableContents translatable) {
                    whereFrom = "%s | translatable".formatted(translatable.getKey());
                }

                VeggyCraft.LOGGER.warn("#wherefrom: %s", whereFrom);
            });

            return;
        }
         */

        if (stack.isPresent() && thisModClockStack.getItem() == stack.get().getItem()) {
            IJeiHelpers jeiHelpers = Internal.getJeiRuntime().getJeiHelpers();
            IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
            modIdHelper.getModNameForTooltip(typedIngredient).ifPresent(before ->
                this.add(Component.literal(modIdHelper.getFormattedModNameForModId("Minecraft")).withStyle(before.getStyle()))
            );

            IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
            renderHelper.renderTooltip(guiGraphics, lines, x, y, ingredientRenderer.getFontRenderer(Minecraft.getInstance(), typedIngredient.getIngredient()), thisModClockStack);

            ci.cancel();
        }
    }
}
