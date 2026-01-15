package net.klayil.veggycraft.neoforge.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.datagen.ColoursList;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.data.models.model.ItemModelUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiConsumer;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class VeggyModModelProvider extends ModelProvider {
    private ItemModelGenerators generalItemModelGenerators;

    public VeggyModModelProvider(PackOutput output) {
        super(output);
    }

    private ResourceLocation modLoc(String path) {
        return ResourceLocation.fromNamespaceAndPath("veggycraft", path);
    }

    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.run();

        var molassesBlock_ = ModBlocks.MOLASSES_BLOCK.getOrNull();

        if (molassesBlock_ != null) {
            Block molassesBlock = molassesBlock_;
//
//            TextureMapping textureMapping = new TextureMapping()
//                    .put(ModModelsTemplates.PARTICLE,
//                            TextureMapping.getBlockTexture(molassesBlock, "_top"))
//                    .put(ModModelsTemplates.DOWN,
//                            TextureMapping.getBlockTexture(molassesBlock, "_bottom"))
//                    .put(ModModelsTemplates.UP,
//                            TextureMapping.getBlockTexture(molassesBlock, "_top"))
//                    .put(ModModelsTemplates.SIDE,
//                            TextureMapping.getBlockTexture(molassesBlock, "_side"));
//
//            ResourceLocation modelId = ModelLocationUtils.getModelLocation(molassesBlock);
//
//            ModModelsTemplates.CUSTOM_MOLASSES_TEMPLATE.create(
//                    modelId,
//                    textureMapping,
//                    blockModelGenerators.modelOutput
//            );

            boolean carnauba_generating = true;

            for (RegistrySupplier<Block> blockRegistrySupplier : ModBlocks.CARNAUBA_WOODS.values()) {
                if (blockRegistrySupplier.getOrNull() == null) {
                    carnauba_generating = false;
                    break;
                }
            }

            if (carnauba_generating) {
                blockModelGenerators.woodProvider(ModBlocks.CARNAUBA_WOODS.get("log").get())
                        .logWithHorizontal(ModBlocks.CARNAUBA_WOODS.get("log").get())
                        .wood(ModBlocks.CARNAUBA_WOODS.get("wood").get());

                blockModelGenerators.woodProvider(ModBlocks.CARNAUBA_WOODS.get("stripped_log").get())
                        .logWithHorizontal(ModBlocks.CARNAUBA_WOODS.get("stripped_log").get())
                        .wood(ModBlocks.CARNAUBA_WOODS.get("stripped_wood").get());


                Block planks = ModBlocks.CARNAUBA_WOODS.get("planks").get();
                blockModelGenerators.createTrivialCube(planks);
                blockModelGenerators.registerSimpleItemModel(planks, ModelLocationUtils.getModelLocation(planks));

                Block leaves = ModBlocks.CARNAUBA_WOODS.get("leaves").get();
                blockModelGenerators.createTrivialBlock(leaves, TexturedModel.LEAVES);
                blockModelGenerators.registerSimpleItemModel(leaves, ModelLocationUtils.getModelLocation(leaves));

                SaplingBlock sapling = (SaplingBlock) ModBlocks.CARNAUBA_WOODS.get("sapling").get();

                blockModelGenerators.createCrossBlock(sapling,  BlockModelGenerators.PlantType.TINTED);

                blockModelGenerators.registerSimpleItemModel(
                        sapling.asItem(), BlockModelGenerators.PlantType.NOT_TINTED.createItemModel(
                                blockModelGenerators, sapling
                        )
                );
            }

            var theBlockItem = ModBlocks.MOLASSES_BLOCK_ITEM.getOrNull();
//
            assert theBlockItem != null;

//            blockModelGenerators.createFlatItemModelWithBlockTexture(theBlockItem, molassesBlock);

            blockModelGenerators.createFlatItemModelWithBlockTexture(theBlockItem, molassesBlock);

//            blockModelGenerators.createTrivialCube(molassesBlock);

//            blockModelGenerators.createFlatItemModelWithBlockTexture(theBlockItem, molassesBlock);
//            blockModelGenerators.registerSimpleItemModel(theBlockItem, blockModelGenerators.createFlatItemModelWithBlockTexture(theBlockItem, molassesBlock));

//            blockModelGenerators.registerSimpleFlatItemModel(molassesBlock);

//            VeggyCraft.LOGGER.info("#CREATED: molasses block model");
        }

        for (int color_id = 0; color_id < 16; color_id++) {
            RegistrySupplier<Block> curBlockSupplier = ModBlocks.modalFabrics.get(color_id);

            String colorName = ColoursList.listOfColours[color_id];
            ResourceLocation woolId = ResourceLocation.withDefaultNamespace(colorName + "_wool");
            Block curWool = BuiltInRegistries.BLOCK.getValue(woolId);

//            blockModelGenerators.copyModel(curWool, curBlockSupplier.get());

            MultiVariant multivariant = plainVariant(ModelLocationUtils.getModelLocation(curWool));
            blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(curBlockSupplier.get(), multivariant));

            BlockItem curBlockItem = (BlockItem) BuiltInRegistries.ITEM.getValue(
                    ResourceLocation.fromNamespaceAndPath(
                            VeggyCraft.MOD_ID,
                            curBlockSupplier.getKey().location().getPath()
                    )
            );

//            blockModelGenerators.createFlatItemModelWithBlockTexture(curBlockItem, curWool);

            blockModelGenerators.itemModelOutput.copy(
                curWool.asItem(),
                curBlockItem
            );
        }



//        blockModelGenerators.createTrivialCube(ModBlocks.MOLASSES_BLOCK.get());
//        blockModelGenerators.createFlatItemModelWithBlockTexture(ModBlocks.MOLASSES_BLOCK_ITEM.get(), ModBlocks.MOLASSES_BLOCK.get());


    }

    private void registerFlatItemModel(RegistrySupplier<Item> item, ModelTemplate modelTemplate) {
        this.generalItemModelGenerators.createFlatItemModel(item.get(), modelTemplate);
        this.generalItemModelGenerators.declareCustomModelItem(item.get());
    }

    private ResourceLocation coloredCarbonLayer(int suffixNum) {
        return ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "item/colored_carbon_layer%d".formatted(suffixNum));
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModelGenerators, ItemModelGenerators itemModelGenerators) {

        itemModelGenerators.run();

        this.generalItemModelGenerators = itemModelGenerators;
        generateBlockStateModels(blockModelGenerators);

        ResourceLocation cookendSeitanResourceLocation = ModelLocationUtils.getModelLocation(ModItems.SEITAN_COOKED_BEEF.get());

        this.generalItemModelGenerators.generateLayeredItem(
                cookendSeitanResourceLocation,
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "item/plant_meat"),
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "item/plant_meat_outline"),
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "item/seitan_cooked_beef_extra")
        );

        this.generalItemModelGenerators.itemModelOutput.accept(
                ModItems.SEITAN_COOKED_BEEF.get(),
                ItemModelUtils.tintedModel(
                        cookendSeitanResourceLocation,

                        ItemModelUtils.constantTint(0xd64320),
                        ItemModelUtils.constantTint(0x631c0d),
                        ItemModelUtils.constantTint(0XFFFFFF)
                )
        );


        ResourceLocation theShinyResourceLocation = ModelTemplates.THREE_LAYERED_ITEM.create(
                ModelLocationUtils.getModelLocation(ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get()),
                TextureMapping.layered(
                        coloredCarbonLayer(0),
                        coloredCarbonLayer(1),
                        coloredCarbonLayer(2)
                ),
                this.generalItemModelGenerators.modelOutput);

        this.generalItemModelGenerators.itemModelOutput.accept(
                ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get(),
                ItemModelUtils.tintedModel(
                        theShinyResourceLocation,

                        ItemModelUtils.constantTint(0xffffff),
                        ItemModelUtils.constantTint(0X58FFF2)
                )
        );

        registerFlatItemModel(ModItems.BROWN_SUGAR, ModelTemplates.FLAT_ITEM);
        registerFlatItemModel(ModItems.MOLASSES_BOTTLE, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.THIS_MOD_FLOUR, ModelTemplates.FLAT_ITEM);
        registerFlatItemModel(ModItems.WET_RAW_SEITAN, ModelTemplates.FLAT_ITEM);

//        for (int i = 0; i < 3; i++) {
//            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
//                    VeggyCraft.MOD_ID,
//                    "dry_raw_seitan_%d".formatted(i)
//            ));
//
//            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(
//                    ModelLocationUtils.getModelLocation(item, ""),
//                    TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "dry_raw_seitan")
//                            .withPrefix("item/")),
//                    this.generalItemModelGenerators.modelOutput
//            );
//
//            this.generalItemModelGenerators.declareCustomModelItem(item);
//        }

        registerFlatItemModel(ModItems.DRY_RAW_SEITAN_0, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.BLACK_OF_COAL_CARBON, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.COAL_CARBON_CUTTER, ModelTemplates.FLAT_HANDHELD_ITEM);
        registerFlatItemModel(ModItems.DIAMOND_CARBON_CUTTER, ModelTemplates.FLAT_HANDHELD_ITEM);

        registerFlatItemModel(ModItems.FLOUR_BAG, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.SUGAR_BAG, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.DRIED_MOLASSES, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.CARNAUBA_WAX, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.CARNAUBA_POWDER, ModelTemplates.FLAT_ITEM);

        ModelTemplates.FLAT_ITEM.create(
                ResourceLocation.withDefaultNamespace("carbon_black_dye").withPrefix("item/"),
                TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "carbon_black_dye")
                        .withPrefix("item/")),
                this.generalItemModelGenerators.modelOutput
        );
    }
}