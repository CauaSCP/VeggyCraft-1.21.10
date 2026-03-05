package net.klayil.veggycraft.fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.fabric.blocks.entities.ModBlockEntityTypesFabric;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

public final class VeggyCraftFabric implements ModInitializer {
    private final Map<CustomTabsMethods.ToPutAt, BiConsumer<FabricItemGroupEntries, List<ItemStack>>> map = Map.of(
            CustomTabsMethods.ToPutAt.BEFORE, this::addBefore,
            CustomTabsMethods.ToPutAt.AFTER, this::addAfter
    );

    private abstract class LastMa implements List<String> {}

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.

//        try {
//            var ignore = (LastMa) List.of("");
//        } catch (ClassCastException e) {
//            throw new Error(e.getMessage(), e.getCause());
//
////            Arrays.stream(e.getMessage().split(" ")).forEachOrdered(each -> System.err.printf("`%s`%n", each));
//        }


        VeggyCraft.setMetaDataGen(System.getProperty("fabric-api.dataGen".toLowerCase()) != null);

        VeggyCraft.init();

        BiConsumer<FabricItemGroupEntries, List<ItemStack>> funcConsumer;

        for (ResourceKey<CreativeModeTab> key : CustomTabsMethods.to_add_afters.keySet()) {

            for (List<?> itemsList : CustomTabsMethods.to_add_afters.get(key)) {
                funcConsumer = map.get((CustomTabsMethods.ToPutAt) itemsList.getLast());

                BiConsumer<FabricItemGroupEntries, List<ItemStack>> finalFuncConsumer = funcConsumer;

                ItemGroupEvents.modifyEntriesEvent(key).register(content -> finalFuncConsumer.accept(
                        content,
                        new CustomTabsMethods.ItemStackList(
                                List.of(itemsList.getFirst(), itemsList.get(1)))
                        )
                );
            }
        }

        ModBlockEntityTypesFabric.initBlockEntityTypes();

//        FabricBrewingRecipeRegistryBuilder.BUILD.register(VeggyCraft::doBrewingRecipeRegister);
    }

    public void addAfter(FabricItemGroupEntries content, List<ItemStack> list) {
//        VeggyCraft.LOGGER.warn("#AFT: %s", list.size());
        content.addAfter(list.getFirst(), list.get(1));
    }

    public void addBefore(FabricItemGroupEntries content, List<ItemStack> list) {
//        VeggyCraft.LOGGER.warn("#BFR: %s, %s", list.getFirst(), list.get(1));
        content.addBefore(list.getFirst(), list.get(1));
    }
}
