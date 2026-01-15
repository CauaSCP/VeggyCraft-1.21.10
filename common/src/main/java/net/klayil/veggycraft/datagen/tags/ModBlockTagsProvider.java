package net.klayil.veggycraft.datagen.tags;

import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends TagsProvider {
    private static PackOutput u;
    private static ResourceKey r;
    private static CompletableFuture l;

    public ModBlockTagsProvider(PackOutput output, ResourceKey registryKey, CompletableFuture lookupProvider) {
        super(output, registryKey, lookupProvider);

        u = output;
        r = registryKey;
        l = lookupProvider;
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (int i = 0; i < 16; i++) {
            getOrCreateRawBuilder(ModItemTags.TAG_MODAL_FABRIC_BLOCKS).add(TagEntry.element(ModBlocks.modalFabrics.get(i).getId()));
        }

        getOrCreateRawBuilder(BlockTags.WOOL).add(TagEntry.tag(
                ModItemTags.TAG_MODAL_FABRIC_BLOCKS.location()
        ));

        for (String key : ModBlocks.CARNAUBA_WOODS.keySet()) {
            for (String w : new String[]{"log", "wood"}) {
                if (key.contains(w)) getOrCreateRawBuilder(BlockTags.LOGS_THAT_BURN).add(TagEntry.element(ModBlocks.CARNAUBA_WOODS.get(key).getId()));
            }
        }
    }

    public static TagBuilder getRawBuilder2(TagKey<Block> tag) {
        return new ModBlockTagsProvider(u,r,l).getOrCreateRawBuilder(tag);
    }
}
