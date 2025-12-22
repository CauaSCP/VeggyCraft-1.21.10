package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.CreativeTabRegistry;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public class VeggyCraftTabsCode extends VeggyCraftCreativeTabsToGet {
    public static void initTabs() {
        VeggyCraftCreativeTabsToGet.masterInit();

        Supplier<ItemStack>[] flourAndStuff = new Supplier[] {
                () -> new ItemStack(ModItems.THIS_MOD_FLOUR),
                () -> {
                    ItemStack bundledAtMaximum = new ItemStack(
                            KlayApiModItems.AllKlayApiItems.get("veggycraft:64_items_stacked_of_flour")
                    );
                    bundledAtMaximum.setDamageValue(1);
                    return bundledAtMaximum;
                }
        };

        CreativeTabRegistry.appendStack(REPLACEMENTS, flourAndStuff);

        TABS_THIS_MOD.register();
    }
}
