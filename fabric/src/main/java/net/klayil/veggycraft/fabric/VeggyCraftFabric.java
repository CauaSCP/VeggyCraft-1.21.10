package net.klayil.veggycraft.fabric;

import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public final class VeggyCraftFabric implements ModInitializer {
    private final Map<CustomTabsMethods.ToPutAt, BiConsumer<FabricItemGroupEntries, List>> map = Map.of(
            CustomTabsMethods.ToPutAt.BEFORE, this::addBefore,
            CustomTabsMethods.ToPutAt.AFTER, this::addAfter
    );

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        VeggyCraft.init();

        BiConsumer<FabricItemGroupEntries, List> funcConsumer;

        for (ResourceKey<CreativeModeTab> key : CustomTabsMethods.to_add_afters.keySet()) {

            for (List itemsList : CustomTabsMethods.to_add_afters.get(key)) {
                funcConsumer = map.get(itemsList.getLast());

                BiConsumer<FabricItemGroupEntries, List> finalFuncConsumer = funcConsumer;
                List finalItemsList = itemsList;
                ItemGroupEvents.modifyEntriesEvent(key).register(content -> {
                    finalFuncConsumer.accept(content, finalItemsList);
                });
            }
        }
    }

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void addAfter(FabricItemGroupEntries content, List list) {
        content.addAfter(new ItemStack((Item) list.getFirst()), new ItemStack((RegistrySupplier<Item>) list.get(1)));
    }

    public void addBefore(FabricItemGroupEntries content, List list) {
        content.addBefore(new ItemStack((Item) list.getFirst()), new ItemStack((RegistrySupplier<Item>) list.get(1)));
    }
}
