package net.klayil.veggycraft.fabric.mixin;

import net.klayil.veggycraft.block.HoneyBlockClone;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public class HoneyBlockCloneMixin {
    @Inject(
            method = "isSticky",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void veggycraft$onIsSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (HoneyBlockClone.isStickyHelper(state)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(
            method = "canStickToEachOther",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void veggycraft$onCanStickToEachOther(BlockState state1, BlockState state2, CallbackInfoReturnable<Boolean> cir) {
        if (!HoneyBlockClone.canStickToEachOtherHelper(state1, state2)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
