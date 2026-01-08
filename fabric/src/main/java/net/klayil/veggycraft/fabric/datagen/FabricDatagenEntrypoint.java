package net.klayil.veggycraft.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
//import net.klayil.veggycraft.datagen.recipe.VeggyModRecipeProvider;
//import net.klayil.veggycraft.fabric.datagen.VeggyModModelProvider;

import net.klayil.veggycraft.datagen.ModTagsProvider;
import net.minecraft.core.registries.Registries;

public class FabricDatagenEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricGen) {
        FabricDataGenerator.Pack pack = fabricGen.createPack();

        pack.addProvider(net.klayil.veggycraft.fabric.datagen.VeggyModRecipeProviderFabric.Runner::new);

        pack.addProvider((fabricDataOutput, ignoredFut) -> {
            return new ModTagsProvider(fabricDataOutput, Registries.BLOCK, fabricGen.getRegistries());
        });

        // if (Platform.isFabric()) {
            // pack.addProvider(VeggyModModelProvider::new);
        // }
    }
}
