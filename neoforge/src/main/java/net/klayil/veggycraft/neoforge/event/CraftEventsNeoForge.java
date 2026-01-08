package net.klayil.veggycraft.neoforge.event;

import net.klayil.veggycraft.recipe.SharedCraftHandler;
import net.minecraft.world.inventory.CraftingContainer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

@EventBusSubscriber(
        modid = "veggycraft"
)
public final class CraftEventsNeoForge {
    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
//        if (!(event.getEntity() instanceof ServerPlayer)) return;

        SharedCraftHandler.onCraft((CraftingContainer) event.getInventory(), event.getCrafting());
    }
}
