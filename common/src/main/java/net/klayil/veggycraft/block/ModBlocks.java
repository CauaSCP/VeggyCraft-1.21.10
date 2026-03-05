package net.klayil.veggycraft.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.klayil.veggycraft.world.tree.ModSaplingGenerator;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.*;

import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS;
    public static ArrayList<RegistrySupplier<Block>> modalFabrics;
    public static Supplier<Block> MOLASSES_BLOCK;
    public static Supplier<Item> MOLASSES_BLOCK_ITEM;
    public static HashMap<String, RegistrySupplier<Block>> CARNAUBA_WOODS = new HashMap<>();
    public static RegistrySupplier<Block> STRAW_BED;
    public static RegistrySupplier<Block> EVEN_STRIPPED_BIRCH_LOG;
    public static RegistrySupplier<Block> HAY_NO_STRIP;

    static final String molasses_block_name = "molasses_block";

    private static RegistrySupplier<?>[] createHoneyBlockClone(ResourceKey<CreativeModeTab> ignoreCreativeModeTab, BlockBehaviour.Properties properties) {
        String mod_id = VeggyCraft.MOD_ID;

        ResourceLocation blockLocation = ResourceLocation.fromNamespaceAndPath(mod_id, molasses_block_name+"_as_block");
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(mod_id, molasses_block_name);
        Supplier<Block> supplierBlock = () -> new HoneyBlockClone(
            properties.noOcclusion()
        );

        RegistrySupplier<Block> blockRegistry = BLOCKS.register(blockLocation, supplierBlock);

        KlayApiModBlocks.AllKlayApiBlocks.put(blockRegistry.toString(), blockRegistry);

        Supplier<Item> blockItemSupplier = () -> new BlockItem(blockRegistry.get(),
                KlayApiModItems.baseProperties(itemLocation.getPath(), itemLocation.getNamespace()));
        RegistrySupplier<Item> blockItemRegistry = ModItems.ITEMS.register(itemLocation.getPath(), blockItemSupplier);

        RegistrySupplier<?>[] res = new RegistrySupplier[2];
        res[0] = blockRegistry;
        res[1] = blockItemRegistry;

        return res;
    }

    public static class StringList extends ArrayList<String> implements List<String> {
        public final List<String> value;
        static ArrayList<String> self;

        public StringList(Collection<String> param) {
            super(master(param));
            value = self;
        }

        private static Collection<String> master(Collection<String> param) {
            self = new ArrayList<>(param);
            return param;
        }
    }

    public static void initBlocks() {
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

        HAY_NO_STRIP = BLOCKS.register("__hay_2", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK)
                .strength(-1.0F, 3600000.0F)
                .setId(
                        ResourceKey.create(
                                Registries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(
                                        VeggyCraft.MOD_ID,
                                        "__hay_2"
                                )
                        )
                )));

        STRAW_BED = BLOCKS.register(
                "straw_bed",
                () -> new ModBedBlock(1.9900009, BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(0.2F).noOcclusion().ignitedByLava().pushReaction(PushReaction.DESTROY).setId(
                        ResourceKey.create(
                                Registries.BLOCK,
                                ResourceLocation.fromNamespaceAndPath(
                                        VeggyCraft.MOD_ID,
                                        "straw_bed"
                                )
                        )
                ))
        );

        EVEN_STRIPPED_BIRCH_LOG = BLOCKS.register(
                "even_stripped_birch_log",
                () -> new PillaredFence(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_BIRCH_LOG).noOcclusion()
                        .ignitedByLava().pushReaction(PushReaction.IGNORE)
                        .setId(ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "even_stripped_birch_log")))
                )
        );

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        StringList[] to_loop_in = new StringList[] {
                new StringList(
                        List.of("", "log")
                ),
                new StringList(
                        List.of("", "wood")
                ),
                new StringList(
                        List.of("stripped_", "log")
                ),
                new StringList(
                        List.of("stripped_", "wood")
                )
        };

        String c = "carnauba_";

        for (List<String> name_stuff : to_loop_in) {
            String prefix = name_stuff.getFirst();
            String suffix = name_stuff.getLast();

            String creatingBlockName = prefix+c+suffix;
            String toCopyName = prefix+"jungle_"+suffix;

            Block vanillaBlock = BuiltInRegistries.BLOCK.getValue(
                    ResourceLocation.withDefaultNamespace(toCopyName)
            );

            // VeggyCraft.LOGGER.info("#BLOCK: %s".formatted(vanillaBlock.toString()));

            BlockBehaviour.Properties props = (BuiltInRegistries.BLOCK.getId(vanillaBlock) > 0)
                    ? BlockBehaviour.Properties.ofFullCopy(vanillaBlock)
                    : BlockBehaviour.Properties.of();

            ResourceKey<Block> resourceID = ResourceKey.create(
                    Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(
                            VeggyCraft.MOD_ID,
                            creatingBlockName
                    )
            );

            RegistrySupplier<Block> curBlock = BLOCKS.register(creatingBlockName, () -> new PalmLogBlock(
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
                () -> new PalmLeavesBlock(
                        BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LEAVES)
                            .mapColor(MapColor.COLOR_LIGHT_GREEN)
                            .strength(0.2F)
                            .randomTicks()
                            .sound(SoundType.GRASS)
                            .noOcclusion()
                            .isValidSpawn((s, w, p, entity) -> entity == EntityType.OCELOT || entity == EntityType.PARROT)
                            .isSuffocating((s, w, p) -> false)
                            .isViewBlocking((s, w, p) -> false)
                            .ignitedByLava()
                            .pushReaction(PushReaction.DESTROY)
                            .setId(
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

        RegistrySupplier<?>[] molassesBlockSuppliers = createHoneyBlockClone(
                null,
                BlockBehaviour.Properties.ofFullCopy(Blocks.HONEY_BLOCK).setId(ResourceKey.create(
                        Registries.BLOCK,
                        ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, molasses_block_name)
                )));

        BLOCKS.register();

        MOLASSES_BLOCK = () -> (Block) molassesBlockSuppliers[0].value();
        MOLASSES_BLOCK_ITEM = () -> (Item) molassesBlockSuppliers[1].value();
    }
}