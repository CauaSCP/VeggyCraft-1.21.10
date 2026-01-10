package net.klayil.veggycraft.datagen.tags;

import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.tags.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagEntry;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends TagsProvider {
    public ModItemTagsProvider(PackOutput output, ResourceKey registryKey, CompletableFuture lookupProvider) {
        super(output, registryKey, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (int i = 0; i < 16; i++) {
            getOrCreateRawBuilder(ModItemTags.TAG_MODAL_FABRIC_ITEMS).add(TagEntry.element(ModBlocks.modalFabrics.get(i).getId()));
        }

        getOrCreateRawBuilder(ItemTags.WOOL).add(TagEntry.tag(
                ModItemTags.TAG_MODAL_FABRIC_ITEMS.location()
        ));
    }
}
