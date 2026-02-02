package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.block.ModBedBlock;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {
    @Inject(
            method = "getDrops",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetDrops(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> res = List.of();

        if (!(state.getBlock() instanceof ModBedBlock modBedBlock)) return;

        if (modBedBlock.creativePlayerBroke) {
            modBedBlock.creativePlayerBroke = false;

            cir.setReturnValue(res);

            return;
        }

        if (state.getValue(ModBedBlock.PART) == BedPart.HEAD) res = List.of(
                new ItemStack(state.getBlock().asItem())
        );

        cir.setReturnValue(res);
    }
}
