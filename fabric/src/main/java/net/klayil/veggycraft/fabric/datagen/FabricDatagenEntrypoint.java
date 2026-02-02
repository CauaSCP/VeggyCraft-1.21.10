package net.klayil.veggycraft.fabric.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

import net.klayil.veggycraft.datagen.tags.ModBlockTagsProvider;
import net.klayil.veggycraft.datagen.tags.ModItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;

public class FabricDatagenEntrypoint implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricGen) {
        FabricDataGenerator.Pack pack = fabricGen.createPack();

        pack.addProvider(net.klayil.veggycraft.fabric.datagen.VeggyModRecipeProviderFabric.Runner::new);

        pack.addProvider((fabricDataOutput, ignoredFut) ->
                new ModItemTagsProvider(fabricDataOutput, Registries.ITEM, fabricGen.getRegistries()));

        pack.addProvider((fabricDataOutput, ignoredFut) ->
                new ModBlockTagsProvider(fabricDataOutput, Registries.BLOCK, fabricGen.getRegistries()));
    }
}
