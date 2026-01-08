package net.klayil.veggycraft.fabric.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.SharedCraftHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
    @Inject(
            method = "slotChangedCraftingGrid",
            at = @At("TAIL")
    )
    private static void veggycraft$validateResult(
            AbstractContainerMenu menu, ServerLevel level, Player player, CraftingContainer craftSlots, ResultContainer resultSlots, @Nullable RecipeHolder<CraftingRecipe> recipe,
            CallbackInfo ci
    ) {
        if (!(player instanceof ServerPlayer)) return;

        ItemStack result = resultSlots.getItem(0);
        if (result.isEmpty()) return;

        if (SharedCraftHandler.shouldBlockCraft(craftSlots)) resultSlots.setItem(0, ItemStack.EMPTY);
    }

    @Inject(method = "quickMoveStack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0))
    private void onQuickMoveBeforeCopy(Player player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == 0) { // Result slot
            CraftingMenu handler = (CraftingMenu)(Object)this;

            // Get the crafting container
            CraftingContainer grid = null;
            for (Slot s : handler.slots) {
                if (s.container instanceof CraftingContainer) {
                    grid = (CraftingContainer) s.container;
                    break;
                }
            }

            if (grid != null) {
                Slot resultSlot = handler.getSlot(0);
                ItemStack result = resultSlot.getItem();

                if (!result.isEmpty()) {
                    VeggyCraft.LOGGER.info("Shift-click craft detected, calling handler");
                    SharedCraftHandler.onCraft(grid, result);
                }
            }
        }
    }
}