package net.klayil.veggycraft.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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

    public static void initBlocks() {
        KlayApiModBlocks.initBlocks();

        BLOCKS = KlayApiModBlocks.createBlocksRegister(VeggyCraft.MOD_ID);

        modalFabrics = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            RegistrySupplier<Block> tmp = KlayApiModBlocks.createBlock(
                    "modal_fabric_%02d".formatted(i),
                    CreativeModeTabs.COLORED_BLOCKS,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).setId(ResourceKey.create(
                            Registries.BLOCK,
                            ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "modal_fabric_%02d".formatted(i))
                    )),
                    VeggyCraft.MOD_ID);
            modalFabrics.add(tmp);
        }

        BLOCKS.register();

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        try {
            for (int i = 0; i < 16; i++) {
                fireBlock.setFlammable(modalFabrics.get(i).get(), 30, 60);
            }
        } catch (Exception e) {
            if (!(e.getMessage().contains("Registry Object not present: veggycraft:modal_fabric_"))) throw e;

            VeggyCraft.LOGGER.warn("#ERR `%s` ignored.".formatted(e.getMessage()));
        }
    }
}