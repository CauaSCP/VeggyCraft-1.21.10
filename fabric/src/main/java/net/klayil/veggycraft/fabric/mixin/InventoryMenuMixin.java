package net.klayil.veggycraft.fabric.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.SharedCraftHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// InventoryMenuMixin.java
@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends AbstractContainerMenu {
    protected InventoryMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    // The inventory crafting grid is usually at index 0 in the slots list
    @Inject(method = "quickMoveStack", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;copy()Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0))
    private void onQuickMoveBeforeCopy(Player player, int slot, CallbackInfoReturnable<ItemStack> cir) {
        if (slot == 0) { // Result slot
            InventoryMenu handler = (InventoryMenu)(Object)this;

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