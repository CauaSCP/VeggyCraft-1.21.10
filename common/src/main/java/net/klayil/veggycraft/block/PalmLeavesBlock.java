package net.klayil.veggycraft.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.mixin.PropertiesAccessor;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class PalmLeavesBlock extends LeavesBlock {
    private final Properties properties;
    private BlockState state = null;

    @Nullable public BlockPos logPos = null;
    public static final MapCodec<PalmLeavesBlock> CODEC = simpleCodec(PalmLeavesBlock::new);

    @Override
    public @NotNull MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    public PalmLeavesBlock(Properties properties) {
        super(0.1f, properties);

        this.properties = properties;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        if (state == null) return;

        MapColor mapColor = ((PropertiesAccessor) properties).getMapColor().apply(state);

        ColorParticleOption colorParticleOption = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, mapColor.col);
        ParticleUtils.spawnParticleBelow(level, pos, random, colorParticleOption);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        this.state = state;

        if (logPos == null) {
            setLogPos(level, pos);
            return;
        }

        if ( !(level.getBlockState(logPos).getBlock() instanceof PalmLogBlock) )
            super.randomTick(state, level, pos, random);
    }

    protected void setLogPos(ServerLevel level, BlockPos pos) {
        Block currentBlock;
        Vec3i keepDistances = null;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    currentBlock = level.getBlockState(
                            new BlockPos(
                         dx + pos.getX(),
                         dy + pos.getY(),
                         dz + pos.getZ()
                        )
                    ).getBlock();


                    if (currentBlock instanceof PalmLeavesBlock && (dx != 0 | dz != 0)) {
                        keepDistances = new Vec3i(dx, 0, dz);

                        break;
                    }
                }
            }
        }

        assert keepDistances != null;

        Block hasToBeLog;

        BlockPos newPos = new BlockPos(
             pos.getX() + keepDistances.getX() * 2,
                pos.getY(),
             pos.getZ() + keepDistances.getZ() * 2
        );

        for (int i = 0; i < 32; i++) {
            hasToBeLog = level.getBlockState(newPos).getBlock();

            if (hasToBeLog instanceof PalmLogBlock) break;

            newPos = new BlockPos(
                    newPos.getX() + keepDistances.getX(),
                    newPos.getY(),
                    newPos.getZ() + keepDistances.getZ()
            );
        }

        while (true) {

            if ( !(level.getBlockState(newPos).getBlock() instanceof PalmLogBlock) ) {
                logPos = newPos.below();

                return;
            }

            newPos = newPos.above();
        }
    }
}
