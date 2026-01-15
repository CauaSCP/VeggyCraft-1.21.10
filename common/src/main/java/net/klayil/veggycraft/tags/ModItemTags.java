package net.klayil.veggycraft.tags;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItemTags {
    public static TagKey<Item> TAG_MODAL_FABRIC_ITEMS;
    public static TagKey<Block> TAG_MODAL_FABRIC_BLOCKS;

    public static TagKey<Item> TOOLS_VIA_MOD;

    public static TagKey<Item> SWORDS;
    public static void init() {
        TAG_MODAL_FABRIC_ITEMS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(
                VeggyCraft.MOD_ID,
                "modal_fabric"
        ));

        TAG_MODAL_FABRIC_BLOCKS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(
                VeggyCraft.MOD_ID,
                "modal_fabric_block"
        ));

        SWORDS = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(
                "enchantable/sword"
        ));

        TOOLS_VIA_MOD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(
                VeggyCraft.MOD_ID,
                "vanilla_tools"
        ));
    }
}
