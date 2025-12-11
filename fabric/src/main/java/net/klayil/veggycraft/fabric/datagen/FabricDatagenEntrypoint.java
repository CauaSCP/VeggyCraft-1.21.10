package net.klayil.veggycraft.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.klayil.veggycraft.datagen.recipe.VeggyModRecipeProvider;

import dev.architectury.platform.Platform;

public class FabricDatagenEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricGen) {
        FabricDataGenerator.Pack pack = fabricGen.createPack();

        pack.addProvider(VeggyModRecipeProvider.Runner::new);

        if (Platform.isFabric()) {
            pack.addProvider(VeggyModModelProvider::new);
        }
    }
}
