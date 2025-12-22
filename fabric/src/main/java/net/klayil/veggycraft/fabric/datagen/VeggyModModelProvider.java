package net.klayil.veggycraft.fabric.datagen;

import net.klayil.klay_api.item.KlayApiModItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;


import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.datagen.VeggyModModelProviderCommon;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class VeggyModModelProvider extends FabricModelProvider {
    private ItemModelGenerators generalItemModelGenerators;
    private ResourceLocation curResourceLocation;
    private TextureMapping curTextureMapping;

    public VeggyModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        VeggyModModelProviderCommon metaClass = new VeggyModModelProviderCommon();

        metaClass.generalItemModelGenerators = (Object) itemModelGenerators;

        this.generalItemModelGenerators = (ItemModelGenerators) metaClass.generalItemModelGenerators;

        metaClass.generateItemModelsCommon();

        for (Object[] arr : metaClass.modelTemplatesOfFlatItemsToCreate) {
            curResourceLocation = (ResourceLocation) arr[0];
            curTextureMapping = (TextureMapping) arr[1];

            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(curResourceLocation, curTextureMapping, this.generalItemModelGenerators.modelOutput);
        }

//        VeggyCraft.LOGGER.info("# SIZES 01: %d;%d;%d;\n# SIZES 02: %d;%d;%d;".formatted(
//            metaClass.sizes[0], metaClass.sizes[1], metaClass.sizes[2],
//            metaClass.potionsToGenerate.size(), metaClass.flatItemModelsToCreate.size(), metaClass.customModelItemToDeclare.size()
//        ));

        for (Item item : metaClass.potionsToGenerate) {
            VeggyCraft.LOGGER.info("#ITEM: "+item.toString());

            this.generalItemModelGenerators.generatePotion(item);
        }

        for (Item item : metaClass.flatItemModelsToCreate) {
            VeggyCraft.LOGGER.info("#ITEM: "+item.toString());

            this.generalItemModelGenerators.createFlatItemModel(item, net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM);
        }

        for (Item item : metaClass.customModelItemToDeclare) {
            VeggyCraft.LOGGER.info("#ITEM: "+item.toString());

            this.generalItemModelGenerators.declareCustomModelItem(item);
        }
    }
}