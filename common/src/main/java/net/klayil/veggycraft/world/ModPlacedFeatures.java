package net.klayil.veggycraft.world;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> CARNAUBA_PLACED_KEY = ResourceKey.create(
            Registries.PLACED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "carnauba"
            )
    );

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        context.register(
                CARNAUBA_PLACED_KEY,
                new PlacedFeature(
                        configuredFeatures.getOrThrow(ModConfiguredFeatures.CARNAUBA_KEY),
                        VegetationPlacements.treePlacement(
                                PlacementUtils.countExtra(2, 0.1f, 2),
                                ModBlocks.CARNAUBA_WOODS.get("sapling").get()
                        )
                )
        );
    }

}
