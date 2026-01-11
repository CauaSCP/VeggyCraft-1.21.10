package net.klayil.veggycraft.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.klayil.veggycraft.BuiltinResourcePacks;
import net.klayil.veggycraft.VeggyCraft;

import net.klayil.veggycraft.block.ModBlocks;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;


public final class VeggyCraftFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.putBlock(ModBlocks.MOLASSES_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);

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
