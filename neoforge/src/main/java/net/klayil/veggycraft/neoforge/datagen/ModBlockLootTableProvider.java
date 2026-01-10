package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        ResourceLocation drop_id;

        for (int id_num = 1; id_num <= 16; id_num++) {
            drop_id = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "modal_fabric_%02d".formatted(id_num));

            dropOther(
                    BuiltInRegistries.BLOCK.getValue(drop_id),
                    BuiltInRegistries.ITEM.getValue(drop_id)
            );
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        ArrayList<Block> knownBlocks = new ArrayList<>();

        ModBlocks.BLOCKS.forEach(blockHolder -> {
            knownBlocks.add(blockHolder.get());
        });

        return knownBlocks;
    }
}
