package net.klayil.veggycraft.mixin;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBedBlock;
import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelAccessor, AutoCloseable {
    @Override
    public void levelEvent(int type, BlockPos pos, int data) {
        Block block = this.getBlockState(pos).getBlock();

        if (
            type == 2001
                &&
            BuiltInRegistries.BLOCK.getKey(block) == BuiltInRegistries.BLOCK.getKey(ModBlocks.STRAW_BED.get())
        ) {

            this.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1f, 1f);

            if (((Level) (Object) this) instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.HAY_NO_STRIP.get().defaultBlockState()),
                    pos.getX() +.5, pos.getY() +.5, pos.getZ() +.5,
                        133, .233, .19, .243, .12
                );
            } else {
                assert ((Level) (Object) this) instanceof ServerLevel;
            }

            return;
        }
        LevelAccessor.super.levelEvent(type, pos, data);
    }
}