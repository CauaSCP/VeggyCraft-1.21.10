package net.klayil.veggycraft.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModBlocks {
    public static DeferredRegister<Block> BLOCKS;
    public static ArrayList<RegistrySupplier<Block>> modalFabrics;
    public static RegistrySupplier<Block> MOLASSES_BLOCK;
    public static RegistrySupplier<Item> MOLASSES_BLOCK_ITEM;

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

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

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