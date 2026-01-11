package net.klayil.veggycraft.item.tabs;

import dev.architectury.registry.CreativeTabRegistry;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.klayil.veggycraft.item.ModItems.*;

public class VeggyCraftTabsCode extends VeggyCraftCreativeTabsToGet {
    public static Runnable initAfterItems;

    public static void initTabs() {
        VeggyCraftCreativeTabsToGet.masterInit();

        // ArrayList<Supplier<ItemStack>>
        var _flourAndStuff = new ArrayList<>(
                Arrays.stream(new Supplier[] {
                        () -> new ItemStack(BLACK_OF_COAL_CARBON),
                        () -> BLACK_DYE_STACK,
                        () -> new ItemStack(THIS_MOD_FLOUR),
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
                        () -> new ItemStack(BROWN_SUGAR),
                        () -> new ItemStack(MOLASSES_BOTTLE),
                }).toList()
        );

        _flourAndStuff.add(
                () -> new ItemStack(ModBlocks.MOLASSES_BLOCK_ITEM)
        );


        _flourAndStuff.add(
                () -> new ItemStack(
                        BuiltInRegistries.ITEM.getValue(
                                ResourceLocation.fromNamespaceAndPath(
                                        VeggyCraft.MOD_ID,
                                        ModBlocks.modalFabrics.getFirst().getKey().location().getPath()
                                )
                        )
                )
        );

        Supplier<ItemStack>[] flourAndStuff = _flourAndStuff.toArray(new Supplier[_flourAndStuff.size()]);


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
