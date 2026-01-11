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

import net.minecraft.world.item.CreativeModeTab.TabVisibility;

@Mod(VeggyCraft.MOD_ID)
public final class VeggyCraftNeoForge {
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

        VeggyCraft.LOGGER.warn("#Size: %d".formatted(CustomTabsMethods.to_add_afters.size()));

        if (!CustomTabsMethods.to_add_afters.containsKey(event.getTabKey())) return;

        for (List<?> itemsList : CustomTabsMethods.to_add_afters.get(event.getTabKey())) {
            event.insertAfter(new ItemStack((Item) itemsList.getFirst()), new ItemStack((RegistrySupplier<Item>) itemsList.get(1)), TabVisibility.PARENT_AND_SEARCH_TABS); // TabVisibility.PARENT_AND_SEARCH_TABS
        }
    }
}
