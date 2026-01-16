package net.klayil.veggycraft.block;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet;
import net.klayil.veggycraft.mixin.FireBlockAccessor;
import net.klayil.veggycraft.world.tree.ModSaplingGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS;
    public static ArrayList<RegistrySupplier<Block>> modalFabrics;
    public static RegistrySupplier<Block> MOLASSES_BLOCK;
    public static RegistrySupplier<Item> MOLASSES_BLOCK_ITEM;
    public static HashMap<String, RegistrySupplier<Block>> CARNAUBA_WOODS = new HashMap<>();
    public static RegistrySupplier<Block> STRAW_BED;

    private static RegistrySupplier[] createHoneyBlockClone(String name, ResourceKey<CreativeModeTab> creativeModeTab, BlockBehaviour.Properties properties) {
        String mod_id = VeggyCraft.MOD_ID;

        ResourceLocation blockLocation = ResourceLocation.fromNamespaceAndPath(mod_id, "enblocked_"+name);
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(mod_id, name);
        Supplier<Block> supplierBlock = () -> new HoneyBlockClone(properties);

        RegistrySupplier<Block> blockRegistry = BLOCKS.register(blockLocation, supplierBlock);

        KlayApiModBlocks.AllKlayApiBlocks.put(blockRegistry.toString(), blockRegistry);

        Supplier<Item> blockItemSupplier = () -> new BlockItem(blockRegistry.get(),
                KlayApiModItems.baseProperties(itemLocation.getPath(), itemLocation.getNamespace()));
        RegistrySupplier<Item> blockItemRegistry = ModItems.ITEMS.register(itemLocation.getPath(), blockItemSupplier);

        RegistrySupplier[] res = new RegistrySupplier[2];
        res[0] = blockRegistry;
        res[1] = blockItemRegistry;

        return res;
    }

    public static void initBlocks() {
//        KlayApiModBlocks.initBlocks();

        BLOCKS = KlayApiModBlocks.createBlocksRegister(VeggyCraft.MOD_ID);

        modalFabrics = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            RegistrySupplier<Block> tmp = KlayApiModBlocks.createBlock(
                    "modal_fabric_%02d".formatted(i),
                    null,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "modal_fabric_%02d".formatted(i))
                    )),
                    VeggyCraft.MOD_ID);
            modalFabrics.add(tmp);
        }

        STRAW_BED = BLOCKS.register(
                "straw_bed",
                () -> new ModBedBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).setId(
                        ResourceKey.create(
                                Registries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(
                                        VeggyCraft.MOD_ID,
                                        "straw_bed"
                                )
                        )
                ))
        );

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        List<String>[] to_loop_in = new List[] {
                List.of("", "log"),
                List.of("", "wood"),
                List.of("stripped_", "log"),
                List.of("stripped_", "wood")
        };

        String c = "carnauba_";

        for (List<String> _name_stuff : to_loop_in) {
            String prefix = _name_stuff.getFirst();
            String suffix = _name_stuff.getLast();

            String creatingBlockName = prefix+c+suffix;
            String toCopyName = prefix+"jungle_"+suffix;

            Block vanillaBlock = BuiltInRegistries.BLOCK.getValue(
                    ResourceLocation.withDefaultNamespace(toCopyName)
            );

            VeggyCraft.LOGGER.info("#BLOCK: %s".formatted(vanillaBlock.toString()));

            BlockBehaviour.Properties props = (vanillaBlock != null)
                    ? BlockBehaviour.Properties.ofFullCopy(vanillaBlock)
                    : BlockBehaviour.Properties.of();

            ResourceKey<Block> resourceID = ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(
                            VeggyCraft.MOD_ID,
                            creatingBlockName
                    )
            );

            RegistrySupplier<Block> curBlock = BLOCKS.register(creatingBlockName, () -> new RotatedPillarBlock(
                    props.setId(resourceID)
            ));


            RegistrySupplier<Item> curItem = ModItems.ITEMS.register(creatingBlockName, ( (Supplier<? extends Item>) () -> new BlockItem(curBlock.get()
                    , KlayApiModItems.baseProperties(creatingBlockName, VeggyCraft.MOD_ID
            )) ));

            VeggyCraft.LOGGER.info("#CurItem: %s".formatted(curItem.toString()));

            CARNAUBA_WOODS.put(prefix+suffix, curBlock);

            CustomTabsMethods.addToTab(CreativeModeTabs.BUILDING_BLOCKS, CustomTabsMethods.BEFORE, curItem, Items.CRIMSON_STEM);
        }


        String l = c+"leaves";
        CARNAUBA_WOODS.put("leaves", BLOCKS.register(l,
                () -> new TintedParticleLeavesBlock(0.01F, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES).setId(
                        ResourceKey.create(
                          Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, l)
                        )
                ))
        ));


        String p = c+"planks";
        CARNAUBA_WOODS.put("planks", KlayApiModBlocks.createBlock(p,
 null,
                KlayApiModBlocks.baseProperties(p, VeggyCraft.MOD_ID)
                        .instrument(NoteBlockInstrument.BASS).strength(3.0F)
                        .noOcclusion().ignitedByLava()
                        .pushReaction(PushReaction.DESTROY),
                VeggyCraft.MOD_ID
        ));

        String sap = c+"sapling";
        CARNAUBA_WOODS.put("sapling", BLOCKS.register(sap,
                () -> new SaplingBlock(ModSaplingGenerator.CARNAUBA, BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING).setId(
                        ResourceKey.create(
                                Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, sap)
                        )
                ))
        ));

        for (String suffix : new String[]{"leaves", "sapling"}) {
            String name = c+suffix;

            RegistrySupplier<Item> itemRegistry = ModItems.ITEMS.register(name, ( (Supplier<? extends Item>) () -> new BlockItem(CARNAUBA_WOODS.get(suffix).get()
                    , KlayApiModItems.baseProperties(name, VeggyCraft.MOD_ID
            )) ));

            if (suffix.equals("sapling")) {
                CustomTabsMethods.addToTab(CreativeModeTabs.NATURAL_BLOCKS, CustomTabsMethods.BEFORE, itemRegistry, Items.AZALEA);
            } else {
                CustomTabsMethods.addToTab(CreativeModeTabs.NATURAL_BLOCKS, CustomTabsMethods.AFTER, itemRegistry, Items.FLOWERING_AZALEA_LEAVES);
            }
        }

        try {
            for (int i = 0; i < 16; i++) {
                fireBlock.setFlammable(modalFabrics.get(i).get(), 30, 60);
            }
        } catch (Exception e) {
            if (!(e.getMessage().contains("Registry Object not present: veggycraft:modal_fabric_"))) throw e;

            VeggyCraft.LOGGER.warn("#ERR `%s` ignored.".formatted(e.getMessage()));
        }

        final String molasses_block_id = "molasses_block";
        RegistrySupplier[] molassesBlockSuppliers = createHoneyBlockClone(
                molasses_block_id,
                null,
                BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK).setId(ResourceKey.create(
                        Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, molasses_block_id)
                )));

        MOLASSES_BLOCK = (RegistrySupplier<Block>) molassesBlockSuppliers[0];
        MOLASSES_BLOCK_ITEM = (RegistrySupplier<Item>) molassesBlockSuppliers[1];

        BLOCKS.register();
    }
}