package net.klayil.veggycraft.datagen;

import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ModTagsProvider extends TagsProvider {
    public ModTagsProvider(PackOutput output, ResourceKey registryKey, CompletableFuture lookupProvider) {
        super(output, registryKey, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
//        for (int i = 0; i < 16; i++) {
//            getOrCreateRawBuilder(BlockTags.WOOL).add(TagEntry.element(ModBlocks.modalFabrics.get(i).getId()));
//        }
    }
}
