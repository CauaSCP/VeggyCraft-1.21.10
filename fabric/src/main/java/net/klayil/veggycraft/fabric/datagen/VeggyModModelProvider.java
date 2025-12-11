package net.klayil.veggycraft.fabric.datagen;

import net.klayil.klay_api.item.KlayApiModItems;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;


import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class VeggyModModelProvider extends FabricModelProvider {
    public VeggyModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        final Item flour = ModItems.THIS_MOD_FLOUR.get();

        itemModelGenerators.createFlatItemModel(
            flour,
            ModelTemplates.FLAT_ITEM
        );

        itemModelGenerators.declareCustomModelItem(flour);

        Item sizeEightItem = null;

        for (int size = 8; size <= 64; size+=8) {
            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
                VeggyCraft.MOD_ID,
                "%02d_items_stacked_of_flour".formatted(size)
            ));

            if (size == 8) sizeEightItem = item;

            itemModelGenerators.itemModelOutput.accept(item, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(sizeEightItem)));
            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(sizeEightItem), itemModelGenerators.modelOutput);
        }
    }
}
