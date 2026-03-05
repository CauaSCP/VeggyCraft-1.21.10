package net.klayil.veggycraft.neoforge.mixin;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.InsertableLinkedOpenCustomHashSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent.class)
public class BuildCreativeModeTabContentsEventMixin {
    @Inject(
            method = "assertTargetExists",
            at = @At("HEAD")//,
            // cancellable = true
    ) private void assertTargetExistsOrNot(InsertableLinkedOpenCustomHashSet<ItemStack> setToCheck, ItemStack existingEntry, CallbackInfo ci) {

    }
}
