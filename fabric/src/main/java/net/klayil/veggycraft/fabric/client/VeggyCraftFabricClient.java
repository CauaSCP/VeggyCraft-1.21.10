package net.klayil.veggycraft.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.klayil.veggycraft.BuiltinResourcePacks;
import net.klayil.veggycraft.VeggyCraft;

import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.block.entities.ModBedRenderer;
import net.klayil.veggycraft.fabric.blocks.entities.ModBlockEntityTypesFabric;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class VeggyCraftFabricClient implements ClientModInitializer {
    public interface HereReloadListener extends IdentifiableResourceReloadListener, ResourceManagerReloadListener {}

    @Override
    public void onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener( new HereReloadListener() {
            @Override
            public ResourceLocation getFabricId() {
                // Modern 1.21+ syntax: No 'new ResourceLocation()'
                return ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "texture_loader");
            }

            // 3. This method runs when the Manager is READY
            @Override
            public @NotNull CompletableFuture<Void> reload(PreparableReloadListener.SharedState sharedState, Executor executor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor executor2) {
                ResourceManager resourceManager = sharedState.resourceManager();

                if (!VeggyCraft.isDataGen) VeggyCraft.imageGen(resourceManager);

                return preparationBarrier.wait(Unit.INSTANCE).thenRunAsync(() -> {
                    ProfilerFiller profilerFiller = Profiler.get();
                    profilerFiller.push("listener");
                    this.onResourceManagerReload(resourceManager);
                    profilerFiller.pop();
                }, executor2);
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {

            }
        });

        BlockRenderLayerMap.putBlock(ModBlocks.MOLASSES_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
        BlockRenderLayerMap.putBlock(ModBlocks.CARNAUBA_WOODS.get("sapling").get(), ChunkSectionLayer.CUTOUT);
        BlockRenderLayerMap.putBlock(ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get(), ChunkSectionLayer.CUTOUT);


        ClientLifecycleEvents.CLIENT_STARTED.register(client -> VeggyCraft.enableResourcePack(client, VeggyCraft.animatedClockResourcePack));

        BuiltinResourcePacks.init(() -> ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "veggycraft_overrides"),
                FabricLoader.getInstance()
                        .getModContainer(VeggyCraft.MOD_ID)
                        .orElseThrow(() -> new NoSuchElementException("Couldn't find %s's mod container".formatted(VeggyCraft.MOD_ID))),
                Component.translatable("veggycraft_overrides"),
                ResourcePackActivationType.DEFAULT_ENABLED
        ));

        BlockEntityRenderers.register(
                ModBlockEntityTypesFabric.STRAW_BED.get(),
                ModBedRenderer::new
        );
    }
}
