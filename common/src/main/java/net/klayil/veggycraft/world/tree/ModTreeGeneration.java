package net.klayil.veggycraft.world.tree;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.klayil.veggycraft.world.ModPlacedFeatures;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addProperties(
                (context) -> context.getKey().equals(Biomes.JUNGLE),

                (context, mutable) -> {
                    mutable.getGenerationProperties().addFeature(
                            GenerationStep.Decoration.VEGETAL_DECORATION,
                            ModPlacedFeatures.CARNAUBA_PLACED_KEY
                    ); // Cannot resolve method 'addFeature(Decoration, ResourceKey<ConfiguredFeature<?, ?>>)'
                }
        );
    }
}
