package net.klayil.veggycraft.world.carnauba.custom_code;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.PalmLogBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class CarnaubaTrunkPlacer extends StraightTrunkPlacer {
    public static final MapCodec<CarnaubaTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            (instance) -> trunkPlacerParts(instance).apply(instance, CarnaubaTrunkPlacer::new)
    );

    public CarnaubaTrunkPlacer(int i, int j, int k) {

        super(i, j, k);

    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() {

        return ModTrunkPlacers.CARNAUBA_TRUNK.get();

    }
}
