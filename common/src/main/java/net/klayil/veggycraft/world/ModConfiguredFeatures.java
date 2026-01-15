package net.klayil.veggycraft.world;

import com.google.common.collect.ImmutableList;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.CocoaDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> CARNAUBA_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "carnauba")
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context) {

        context.register(CARNAUBA_KEY, new ConfiguredFeature<>(
                Feature.TREE,
                createStraightBlobTree(
                        ModBlocks.CARNAUBA_WOODS.get("log").get(),
                        ModBlocks.CARNAUBA_WOODS.get("leaves").get(), 4, 2, 0, 2)
                        .ignoreVines().build()
        ));

    }

    private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(
            Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, int radius
    ) {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(logBlock),
                new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
                BlockStateProvider.simple(leavesBlock),
                new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }
}
