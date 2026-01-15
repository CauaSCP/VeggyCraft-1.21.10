package net.klayil.veggycraft.neoforge;

import dev.architectury.registry.registries.RegistrySupplier;
import me.shedaniel.autoconfig.annotation.Config;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.klayil.veggycraft.VeggyCraft;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;

@Mod(VeggyCraft.MOD_ID)
public final class VeggyCraftNeoForge {
    private final Map<CustomTabsMethods.ToPutAt, BiConsumer<BuildCreativeModeTabContentsEvent, List>> map = Map.of(
            CustomTabsMethods.ToPutAt.BEFORE, this::addBefore,
            CustomTabsMethods.ToPutAt.AFTER, this::addAfter
    );

    public VeggyCraftNeoForge(IEventBus modEventBus, ModContainer ignoredModContainer) {
        // Run our common setup.
        VeggyCraft.init();

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

//        modContainer.registerConfig(ModConfig.Type.COMMON, );
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
//        VeggyCraft.LOGGER.info("#Contains tab: %s? ANS: #%s".formatted(
//                event.getTabKey().location().getPath(),
//                CustomTabsMethods.to_add_afters.containsKey(event.getTabKey()))
//        );

        if (!CustomTabsMethods.to_add_afters.containsKey(event.getTabKey())) return;

        BiConsumer<BuildCreativeModeTabContentsEvent, List> funcConsumer;

        for (List<?> itemsList : CustomTabsMethods.to_add_afters.get(event.getTabKey())) {
            funcConsumer = map.get(itemsList.getLast());
            funcConsumer.accept(event, itemsList);

//            event.insertAfter(new ItemStack((Item) itemsList.getFirst()), new ItemStack((RegistrySupplier<Item>) itemsList.get(1)), TabVisibility.PARENT_AND_SEARCH_TABS); // TabVisibility.PARENT_AND_SEARCH_TABS
        }
    }

    public void addAfter(BuildCreativeModeTabContentsEvent ev, List list) {
        ev.insertAfter(new ItemStack((Item) list.getFirst()), new ItemStack((RegistrySupplier<Item>) list.get(1)), TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public void addBefore(BuildCreativeModeTabContentsEvent ev, List list) {
        ev.insertBefore(new ItemStack((Item) list.getFirst()), new ItemStack((RegistrySupplier<Item>) list.get(1)), TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
