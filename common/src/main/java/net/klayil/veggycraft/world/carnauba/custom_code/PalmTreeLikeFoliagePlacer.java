package net.klayil.veggycraft.world.carnauba.custom_code;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class PalmTreeLikeFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<FoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(
            inst -> foliagePlacerParts(inst).apply(inst, PalmTreeLikeFoliagePlacer::new)
    );

    public PalmTreeLikeFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacers.PALM_TREE_PLACER.get();
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config, int maxFreeHeight, FoliageAttachment attachment, int foliageHeight, int radius, int offset) {
        BlockPos center = attachment.pos();

        setter.set(center, config.foliageProvider.getState(random, center));

        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dz = dir[1];

            int maxReach = (dx != 0 && dz != 0) ? radius - 1 : radius;

            for (int d = 1; d <= maxReach; d++) {
                int yOffset = 0;
                if (d == 2) yOffset = 0;
                if (d == 3) yOffset = -1;
                if (d >= 4) yOffset = -2;

                BlockPos leafPos = center.offset(dx * d, yOffset, dz * d);

                setter.set(leafPos, config.foliageProvider.getState(random, leafPos));
            }
        }
    }
}