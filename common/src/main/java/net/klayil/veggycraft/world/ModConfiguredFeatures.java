package net.klayil.veggycraft.world;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.world.carnauba.custom_code.CarnaubaTrunkPlacer;
import net.klayil.veggycraft.world.carnauba.custom_code.PalmTreeLikeFoliagePlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?,?>> CARNAUBA_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "carnauba")
    );

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?,?>> context) {

        context.register(
                CARNAUBA_KEY, new ConfiguredFeature<>(
                        Feature.TREE,
                        createCarnaubaTree(
                                ModBlocks.CARNAUBA_WOODS.get("log").get(),
                                ModBlocks.CARNAUBA_WOODS.get("leaves").get(),
                                5,
                                2,
                                0,
                                5
                        ).ignoreVines().build()
                )
        );
    }

    private static TreeConfiguration.TreeConfigurationBuilder createCarnaubaTree(
            Block logBlock, Block leavesBlock, int baseHeight, int heightRandA, int heightRandB, int radius
    ) {
        PalmTreeLikeFoliagePlacer foliagePlacer = new PalmTreeLikeFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0));

        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(logBlock),
                new CarnaubaTrunkPlacer(baseHeight, heightRandA, heightRandB),
                BlockStateProvider.simple(leavesBlock),
                (foliagePlacer),
                new TwoLayersFeatureSize(1, 0, 1)
        );
    }
}
