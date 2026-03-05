package net.klayil.veggycraft.neoforge;

import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.klayil.veggycraft.neoforge.blocks.entites.ModBlockEntityTypesNeoForge;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import net.klayil.veggycraft.VeggyCraft;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import net.minecraft.world.item.CreativeModeTab.TabVisibility;

import static net.klayil.veggycraft.VeggyCraft.comment;

@Mod(VeggyCraft.MOD_ID)
public final class VeggyCraftNeoForge {
    private final Map<CustomTabsMethods.ToPutAt, BiConsumer<BuildCreativeModeTabContentsEvent, List<ItemStack>>> map = Map.of(
            CustomTabsMethods.ToPutAt.BEFORE, this::addBefore,
            CustomTabsMethods.ToPutAt.AFTER, this::addAfter
    );

    public VeggyCraftNeoForge(IEventBus modEventBus, ModContainer ignoredModContainer) {
        VeggyCraft.setMetaDataGen(
                ManagementFactory.getRuntimeMXBean()
                        .getInputArguments()
                        .stream()
                        .anyMatch(arg -> arg.contains("startup.DataClient"))
        );

        VeggyCraft.init();

//        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

        ModBlockEntityTypesNeoForge.initBlockEntityTypes();
    }

//    @SubscribeEvent
//    public static void doBrewingRecipeRegisterNeoForge(RegisterBrewingRecipesEvent event) {
////        VeggyCraft.doBrewingRecipeRegister(event.getBuilder());
//    }

//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//
//    }

    private void addCreative(BuildCreativeModeTabContentsEvent event){
        comment("""
VeggyCraft.LOGGER.info("#Contains tab: %s? ANS: #%s".formatted(
        event.getTabKey().location().getPath(),
        CustomTabsMethods.to_add_afters.containsKey(event.getTabKey()))
);
        """);

        if (!CustomTabsMethods.to_add_afters.containsKey(event.getTabKey())) return;

        BiConsumer<BuildCreativeModeTabContentsEvent, List<ItemStack>> funcConsumer;

        for (List<?> itemsList : CustomTabsMethods.to_add_afters.get(event.getTabKey())) {
            funcConsumer = map.get((CustomTabsMethods.ToPutAt) itemsList.getLast());

            funcConsumer.accept(
                    event,
                    new CustomTabsMethods.ItemStackList(
                        List.of(itemsList.getFirst(), itemsList.get(1))
                    )
            );
        }
    }

    public void addAfter(BuildCreativeModeTabContentsEvent ev, List<ItemStack> list) {
        ev.insertAfter(list.getFirst(), list.get(1), TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    public void addBefore(BuildCreativeModeTabContentsEvent ev, List<ItemStack> list) {
        ev.insertBefore(list.getFirst(), list.get(1), TabVisibility.PARENT_AND_SEARCH_TABS);
    }
}
