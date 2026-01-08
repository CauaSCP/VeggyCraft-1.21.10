package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.CreativeTabRegistry;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.klayil.veggycraft.item.ModItems.*;

public class VeggyCraftTabsCode extends VeggyCraftCreativeTabsToGet {
    public static Runnable initAfterItems;

    public static void initTabs() {
        VeggyCraftCreativeTabsToGet.masterInit();

        Supplier<ItemStack>[] flourAndStuff = new Supplier[] {

                () -> new ItemStack(BLACK_OF_COAL_CARBON),
                () -> BLACK_DYE_STACK,
                () -> new ItemStack(ModItems.THIS_MOD_FLOUR),
                () -> {
                    ItemStack bundledAtMaximum = new ItemStack(
                            FLOUR_BAG
//                            KlayApiModItems.AllKlayApiItems.get("veggycraft:64_items_stacked_of_flour")
                    );
                    bundledAtMaximum.set(ModDataComponentTypes.HEALTH.get(), new ModDataComponentTypes.ItemHealth(64, 64));
                    return bundledAtMaximum;
                },
                () -> {
                    ItemStack dryRawSeitan = new ItemStack(DRY_RAW_SEITAN_0);
                    dryRawSeitan.set(ModDataComponentTypes.HEALTH.get(), new ModDataComponentTypes.ItemHealth(0, 3));
                    return dryRawSeitan;
                },
                () -> new ItemStack(WET_RAW_SEITAN),
                () -> new ItemStack(SEITAN_COOKED_BEEF),

        };

        Supplier<ItemStack>[] carbonDyingStuff = new Supplier[] {

                () -> new ItemStack(Items.COAL),
                () -> new ItemStack(Items.CHARCOAL),
                () -> new ItemStack(BLACK_OF_COAL_CARBON),
                () -> new ItemStack(SHINY_OF_DIAMOND_COAL_CARBON)

        };


        CreativeTabRegistry.appendStack(REPLACEMENTS, flourAndStuff);
        initAfterItems = () -> CreativeTabRegistry.appendStack(CARBON_AND_DYES_TAB, carbonDyingStuff);

        TABS_THIS_MOD.register();
    }
}
