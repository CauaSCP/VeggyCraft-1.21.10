package net.klayil.veggycraft.fabric.datagen;

//import net.klayil.klay_api.item.KlayApiModItems;

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
import net.minecraft.world.item.Items;

import net.minecraft.data.PackOutput;

import java.util.ArrayList;

public class VeggyModModelProvider extends FabricModelProvider {
    private ItemModelGenerators generalItemModelGenerators;

    private ResourceLocation curResourceLocation;
    private TextureMapping curTextureMapping;

    public VeggyModModelProvider(PackOutput output) {super( (FabricDataOutput) output);}

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
    }

//    public class HereModelTemplates {
//        public class _FlatItem {
//            public ResourceLocation create(ResourceLocation modelLocation, TextureMapping textureMapping) {
//                return net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(modelLocation, textureMapping, generalItemModelGenerators.modelOutput);
//            }
//        }
//
//        public _FlatItem FLAT_ITEM = new _FlatItem();
//    }

//    public HereModelTemplates ModelTemplates = new HereModelTemplates();

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        VeggyModModelProviderCommon metaClass = new VeggyModModelProviderCommon();

        metaClass.generalItemModelGenerators = (Object) itemModelGenerators;

        this.generalItemModelGenerators = (ItemModelGenerators) metaClass.generalItemModelGenerators;

        for (Item item : metaClass.customModelItemToDeclare) {
            this.generalItemModelGenerators.generatePotion(item);
        }

        for (Item item : metaClass.customModelItemToDeclare) {
            this.generalItemModelGenerators.createFlatItemModel(item, net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM);
        }

        for (Item item : metaClass.customModelItemToDeclare) {
            this.generalItemModelGenerators.declareCustomModelItem(item);
        }

        for (Object[] arr : metaClass.modelTemplatesOfFlatItemsToCreate) {
            curResourceLocation = (ResourceLocation) arr[0];
            curTextureMapping = (TextureMapping) arr[1];

            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(curResourceLocation, curTextureMapping, this.generalItemModelGenerators.modelOutput);
        }

//        final Item flour = ModItems.THIS_MOD_FLOUR.get();
//
//        itemModelGenerators.generatePotion(Items.POTION);
//
////        itemModelGenerators.createFlatItemModel(
////            flour,
////            ModelTemplates.FLAT_ITEM
////        );
//
//        itemModelGenerators.declareCustomModelItem(flour);
//
//        for (int size = 8; size <= 64; size+=8) {
//            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
//                VeggyCraft.MOD_ID,
//                "%02d_items_stacked_of_flour".formatted(size)
//            ));
//
//            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item, ""), TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_flour_in_bundle_all_cases").withPrefix("item/")), itemModelGenerators.modelOutput);
//
//            itemModelGenerators.declareCustomModelItem(item);
//        }
    }
}
