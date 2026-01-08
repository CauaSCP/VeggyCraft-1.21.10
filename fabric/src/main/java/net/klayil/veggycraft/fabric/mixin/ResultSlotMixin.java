package net.klayil.veggycraft.fabric.mixin;

import net.klayil.veggycraft.recipe.SharedCraftHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {

    @Shadow @Final
    private CraftingContainer craftSlots;

    private boolean shiftUsed;

    @Inject(
            method = "onTake",
            at = @At("HEAD")
    )
    private void veggycraft$onCraft(
            Player player,
            ItemStack stack,
            CallbackInfo ci
    ) {
//        if (!(player instanceof ServerPlayer)) return;

        if (!shiftUsed) {
            SharedCraftHandler.onCraft(craftSlots, stack);
        }

        shiftUsed = false;
    }

    @Inject(method = "onQuickCraft", at = @At("HEAD"))
    private void veggycraft$onQuickCraft(ItemStack stack, int amount, CallbackInfo ci) {
        // This method is ONLY called for shift-click crafts
        shiftUsed = true;
    }
}