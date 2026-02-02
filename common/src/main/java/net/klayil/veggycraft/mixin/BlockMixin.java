package net.klayil.veggycraft.mixin;

//import net.klayil.veggycraft.block.ModBlocks;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
            method = "spawnDestroyParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onSpawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state, CallbackInfo ci) {
        final ResourceLocation blockResLoc = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (blockResLoc == BuiltInRegistries.BLOCK.getKey(ModBlocks.STRAW_BED.get())) {
            ci.cancel();
        }
    }
}
