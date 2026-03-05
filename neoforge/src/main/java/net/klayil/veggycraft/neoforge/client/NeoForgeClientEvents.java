package net.klayil.veggycraft.neoforge.client;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.neoforge.blocks.entites.ModBlockEntityTypesNeoForge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.klayil.veggycraft.block.entities.ModBedRenderer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@EventBusSubscriber(
        modid = VeggyCraft.MOD_ID,
        value = Dist.CLIENT
)
public final class NeoForgeClientEvents {
    @SubscribeEvent
    public static void registerPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

        // get a ResourceManager here

        var packPath = ModList.get()
                .getModFileById(VeggyCraft.MOD_ID)
                .getFile()
                .getFilePath()
                .resolve("resourcepacks/veggycraft_overrides");

        PackLocationInfo location = new PackLocationInfo(
                VeggyCraft.MOD_ID + ":overrides",
                Component.translatable("veggycraft_overrides"),
                PackSource.BUILT_IN,
                Optional.empty()
        );

        Pack.ResourcesSupplier resources = new Pack.ResourcesSupplier() {
            @Override
            public @NotNull PackResources openPrimary(@NotNull PackLocationInfo info) {
                return new PathPackResources(info, packPath);
            }

            @Override
            public @NotNull PackResources openFull(@NotNull PackLocationInfo info, Pack.@NotNull Metadata metadata) {
                return new PathPackResources(info, packPath);
            }
        };

        PackSelectionConfig selectionConfig = new PackSelectionConfig(true, Pack.Position.TOP, true);

        Pack pack = Pack.readMetaAndCreate(location, resources, PackType.CLIENT_RESOURCES, selectionConfig);
        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }


    @SubscribeEvent
    public static void registerBERs(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(
                ModBlockEntityTypesNeoForge.STRAW_BED.get(),
                ModBedRenderer::new
        );
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        VeggyCraft.imageGen(rm);

        VeggyCraft.enableResourcePack(Minecraft.getInstance(), VeggyCraft.animatedClockResourcePack);

//        ModBlocks.MOLASSES_BLOCK

//        event.enqueueWork(() -> {
//            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MOLASSES_BLOCK.get(), ChunkSectionLayer.TRANSLUCENT);
//            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CARNAUBA_WOODS.get("sapling").get(), ChunkSectionLayer.CUTOUT);
//            ItemBlockRenderTypes.setRenderLayer(ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get(), ChunkSectionLayer.CUTOUT);
//        });
    }
}