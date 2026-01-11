package net.klayil.veggycraft.fabric;

import dev.architectury.registry.CreativeTabRegistry;
import net.fabricmc.api.ModInitializer;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.world.item.CreativeModeTabs;

public final class VeggyCraftFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        VeggyCraft.init();
    }
}
