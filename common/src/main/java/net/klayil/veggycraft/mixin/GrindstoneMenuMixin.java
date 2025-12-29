package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneMenuMixin {
    @Inject(
            method = "createResult",
            at = @At("TAIL")
    ) private void veggycraft$blockGrindStone(CallbackInfo _ci) {
        GrindstoneMenu self = (GrindstoneMenu)(Object)this;

        if (self.getSlot(2).getItem().isEmpty()) return;

        if (
            self.getSlot(0).getItem().getItem() instanceof RepairableItemsExtension repairableItem
            && repairableItem.getRepairItem() == null
        ) self.getSlot(2).set(ItemStack.EMPTY);
    }
}
