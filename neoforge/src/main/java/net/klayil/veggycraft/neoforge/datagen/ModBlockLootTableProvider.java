package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Optional;
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

        dropOther(
                ModBlocks.MOLASSES_BLOCK.get(),
                ModBlocks.MOLASSES_BLOCK_ITEM.get()
        );

        for (String key : ModBlocks.CARNAUBA_WOODS.keySet()) {
            @Nullable Block block = ModBlocks.CARNAUBA_WOODS.get(key).getOrNull();

            if (!key.contains("leaves") & block != null) dropSelf(block);
        }


        Optional.ofNullable(ModBlocks.CARNAUBA_WOODS.get("leaves").getOrNull())
                .ifPresent(block -> this.add(block, (leafBlock) -> {
                    return this.createLeavesDrops(
                            leafBlock,
                            ModBlocks.CARNAUBA_WOODS.get("sapling").get(),
                            0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F
                    );
                        })

                ); /*
            ERROR: java.lang.IllegalStateException: Missing loottable 'veggycraft:blocks/carnauba_leaves' for 'veggycraft:carnauba_leaves'
        */
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
