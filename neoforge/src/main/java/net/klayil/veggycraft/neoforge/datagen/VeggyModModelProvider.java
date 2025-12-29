package net.klayil.veggycraft.neoforge.datagen;

import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.minecraft.client.data.models.ModelProvider;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.data.models.model.ItemModelUtils;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.data.models.model.ItemModelUtils.plainModel;

public class VeggyModModelProvider extends ModelProvider {
    private ItemModelGenerators generalItemModelGenerators;

    public VeggyModModelProvider(PackOutput output) {
        super(output);
    }

    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        VeggyCraft.LOGGER.info(blockModelGenerators.toString());

        blockModelGenerators.run();
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
        generateBlockStateModels(blockModelGenerators);

        itemModelGenerators.run();

        this.generalItemModelGenerators = itemModelGenerators;


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

        for (int i = 0; i < 3; i++) {
            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "dry_raw_seitan_%d".formatted(i)
            ));

            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item, ""),
                    TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "dry_raw_seitan")
                            .withPrefix("item/")),
                    this.generalItemModelGenerators.modelOutput
            );

            this.generalItemModelGenerators.declareCustomModelItem(item);
        }

        registerFlatItemModel(ModItems.BLACK_OF_COAL_CARBON, ModelTemplates.FLAT_ITEM);

        registerFlatItemModel(ModItems.COAL_CARBON_CUTTER, ModelTemplates.FLAT_HANDHELD_ITEM);
        registerFlatItemModel(ModItems.DIAMOND_CARBON_CUTTER, ModelTemplates.FLAT_HANDHELD_ITEM);

        for (int size = 8; size <= 64; size+=8) {
            final Item item = BuiltInRegistries.ITEM.getValue(ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "%02d_items_stacked_of_flour".formatted(size)
            ));

            net.minecraft.client.data.models.model.ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item, ""),
                    TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_flour_in_bundle_all_cases")
                            .withPrefix("item/")),
                    this.generalItemModelGenerators.modelOutput
            );

            this.generalItemModelGenerators.declareCustomModelItem(item);
        }

        ModelTemplates.FLAT_ITEM.create(
                ResourceLocation.withDefaultNamespace("carbon_black_dye").withPrefix("item/"),
                TextureMapping.layer0(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "carbon_black_dye")
                        .withPrefix("item/")),
                this.generalItemModelGenerators.modelOutput
        );
    }
}