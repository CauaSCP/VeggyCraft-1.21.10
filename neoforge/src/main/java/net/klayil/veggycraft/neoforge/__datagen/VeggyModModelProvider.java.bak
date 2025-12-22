package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.ModelProvider;

public class VeggyModModelProvider extends ModelProvider {
    public VeggyModModelProvider(PackOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {
        generateBlockStateModels(blockModelGenerators);

        final Item flour = ModItems.THIS_MOD_FLOUR.get();

        itemModelGenerators.generatePotion(Items.POTION);

        itemModelGenerators.createFlatItemModel(
                flour,
                ModelTemplates.FLAT_ITEM
        );

        itemModelGenerators.declareCustomModelItem(flour);

        for (int size = 8; size <= 64; size+=8) {
            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "%02d_items_stacked_of_flour".formatted(size)
            ));

            ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item, ""), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_flour_in_bundle_all_cases").withPrefix("item/")), itemModelGenerators.modelOutput);

            itemModelGenerators.declareCustomModelItem(item);
        }
    }
}