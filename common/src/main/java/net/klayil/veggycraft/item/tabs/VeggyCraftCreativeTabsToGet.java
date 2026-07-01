package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.registries.DeferredRegister;
import net.klayil.klay_api.tabs.KlayApiModTabs;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class VeggyCraftCreativeTabsToGet {
    public static ResourceKey<CreativeModeTab> CARBON_AND_DYES_TAB;

    public static DeferredRegister<CreativeModeTab> TABS_THIS_MOD;

    public static ResourceKey<CreativeModeTab> REPLACEMENTS;

    public static void masterInit() {
        TABS_THIS_MOD = KlayApiModTabs.createTabsRegister(VeggyCraft.MOD_ID);

        CARBON_AND_DYES_TAB = KlayApiModTabs.registerTab("carbon_dying_tab", () -> new ItemStack(ModItems.DIAMOND_CARBON_CUTTER), VeggyCraft.MOD_ID);
        REPLACEMENTS = KlayApiModTabs.registerTab("replacements_tab", () -> new ItemStack(ModItems.SEITAN_COOKED_BEEF), VeggyCraft.MOD_ID);
    }
}
