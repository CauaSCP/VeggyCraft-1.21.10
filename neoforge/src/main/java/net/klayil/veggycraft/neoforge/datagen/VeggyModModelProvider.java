package net.klayil.veggycraft.neoforge.datagen;

import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.datagen.ColoursList;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.data.models.model.ItemModelUtils;

import java.util.concurrent.atomic.AtomicInteger;

import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class VeggyModModelProvider extends ModelProvider {
    private ItemModelGenerators generalItemModelGenerators;

    public VeggyModModelProvider(PackOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.run();

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

        ModelTemplates.FLAT_ITEM.create(
                ResourceLocation.withDefaultNamespace("carbon_black_dye").withPrefix("item/"),
                TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "carbon_black_dye")
                        .withPrefix("item/")),
                this.generalItemModelGenerators.modelOutput
        );
    }
}