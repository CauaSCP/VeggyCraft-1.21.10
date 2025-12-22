package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.registries.DeferredRegister;
import net.klayil.klay_api.tabs.KlayApiModTabs;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class VeggyCraftCreativeTabsToGet {
    public static ResourceKey<CreativeModeTab> REPLACEMENTS;
    public static DeferredRegister<CreativeModeTab> TABS_THIS_MOD;

    public static void masterInit() {
        TABS_THIS_MOD = KlayApiModTabs.createTabsRegister(VeggyCraft.MOD_ID);

        REPLACEMENTS = KlayApiModTabs.registerTab("replacements_tab", () -> new ItemStack(Blocks.DARK_OAK_LEAVES.asItem()), VeggyCraft.MOD_ID);
    }
}
