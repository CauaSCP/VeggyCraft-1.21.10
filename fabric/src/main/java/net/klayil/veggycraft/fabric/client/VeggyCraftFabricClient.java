package net.klayil.veggycraft.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.klayil.veggycraft.BuiltinResourcePacks;
import net.klayil.veggycraft.VeggyCraft;

import net.klayil.veggycraft.recipe.ModRecipes;
import net.klayil.veggycraft.recipe.ReiClientPlugin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public final class VeggyCraftFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

//        VeggyCraft.LOGGER.warn("#PLUGIN: %s".formatted(new net.klayil.veggycraft.compat.JEIPlugin().toString()));

//        ReiClientPlugin a = new ReiClientPlugin();


        BuiltinResourcePacks.init(() -> ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "veggycraft_overrides"),
                FabricLoader.getInstance()
                        .getModContainer(VeggyCraft.MOD_ID)
                        .orElseThrow(),
                Component.translatable("veggycraft_overrides"),
                ResourcePackActivationType.DEFAULT_ENABLED
        ));
    }
}
