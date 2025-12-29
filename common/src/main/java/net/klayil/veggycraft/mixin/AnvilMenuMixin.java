package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.item.RepairableItemsExtension;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Inject(
        method = "createResult",
        at = @At("TAIL")
    )
    private void veggycraft$blockAnvilRepair(CallbackInfo _ci) {
        AnvilMenu self = (AnvilMenu)(Object)this;

        if (self.getSlot(self.getResultSlot()).getItem().isEmpty()) return;

        if (
            self.getSlot(0).getItem().getItem() instanceof RepairableItemsExtension repairableItem
            && repairableItem.getRepairItem() == null
        ) self.getSlot(self.getResultSlot()).set(ItemStack.EMPTY);
    }
}

