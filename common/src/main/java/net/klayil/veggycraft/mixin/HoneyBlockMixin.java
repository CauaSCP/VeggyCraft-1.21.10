package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.HoneyBlockClone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.world.entity.Entity;

@Mixin(HoneyBlock.class)
public class HoneyBlockMixin {

    @Inject(
            method = "showParticles",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void veggycraft$showParticles(Entity entity, int particleCount, CallbackInfo ci) {
        Level level = entity.level();
        // Expand the bounding box slightly (0.05) to catch blocks on the sides
        AABB box = entity.getBoundingBox().inflate(0.05D);

        // Iterate through all blocks touching the entity's box
        Iterable<BlockPos> blockPositions = BlockPos.betweenClosed(
                Mth.floor(box.minX), Mth.floor(box.minY), Mth.floor(box.minZ),
                Mth.floor(box.maxX), Mth.floor(box.maxY), Mth.floor(box.maxZ)
        );

        for (BlockPos pos : blockPositions) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof HoneyBlockClone) {
                if (entity.level().isClientSide()) {
                    BlockState blockState = state;

                    VeggyCraft.LOGGER.warn("#Running");

                    for(int i = 0; i < particleCount; ++i) {
                        entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), entity.getX(), entity.getY(), entity.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
                    }

                }

                ci.cancel();

                break;
            }
        }
    }
}
