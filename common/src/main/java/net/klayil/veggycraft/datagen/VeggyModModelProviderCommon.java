package net.klayil.veggycraft.datagen;

//import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import net.klayil.veggycraft.VeggyCraft;

import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.model.ModelLocationUtils;
//import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

// import net.minecraft.world.item.Items;

import java.util.ArrayList;

public class VeggyModModelProviderCommon {
    public Object generalItemModelGenerators;

    public ArrayList<Item> potionsToGenerate = new ArrayList<>();
    public ArrayList<Item> flatItemModelsToCreate = new ArrayList<>();
    public ArrayList<Item> customModelItemToDeclare = new ArrayList<>();
    public ArrayList<Object[]> modelTemplatesOfFlatItemsToCreate = new ArrayList<>();

    public class HereModelTemplates {
        public class _FlatItem {
            public void create(ResourceLocation modelLocation, TextureMapping textureMapping) {
                modelTemplatesOfFlatItemsToCreate.add(
                        new Object[]{
                            modelLocation,
                            textureMapping
                        }
                );
            }
        }

        public _FlatItem FLAT_ITEM = new _FlatItem();
    }

    public HereModelTemplates ModelTemplates = new HereModelTemplates();

    public void generatePotion(Item item) {
        potionsToGenerate.add(item);
    }

    public void createFlatItemModel(Item item) {
        flatItemModelsToCreate.add(item);
    }

    public void declareCustomModelItem(Item item) {
        customModelItemToDeclare.add(item);
    }

    public void generateItemModelsCommon() {
        final Item flour = ModItems.THIS_MOD_FLOUR.get();

        createFlatItemModel(flour);

        declareCustomModelItem(flour);

        for (int size = 8; size <= 64; size+=8) {
            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "%02d_items_stacked_of_flour".formatted(size)
            ));

                ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item, ""),
                    TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_flour_in_bundle_all_cases").withPrefix("item/"))
                );

                declareCustomModelItem(item);

//                ModelTemplates.FLAT_ITEM.create(
//                        ModelLocationUtils.getModelLocation(item),
//                        TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "item/wheat_flour_in_bundle_all_cases")),
//                        itemModelGenerators.modelOutput
//                );
        }
    }
}