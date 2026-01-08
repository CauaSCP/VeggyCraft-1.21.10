package net.klayil.veggycraft.neoforge.client;

import dev.architectury.registry.registries.DeferredRegister;
import net.klayil.klay_api.KlayApi;
import net.klayil.veggycraft.BuiltinResourcePacks;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.*;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.Optional;

@EventBusSubscriber(
        modid = VeggyCraft.MOD_ID,
        value = Dist.CLIENT
)
public final class NeoForgeBuiltinPacks {
    @SubscribeEvent
    public static void registerPacks(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

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
            public PackResources openPrimary(PackLocationInfo info) {
                return new PathPackResources(info, packPath);
            }

            @Override
            public PackResources openFull(PackLocationInfo info, Pack.Metadata metadata) {
                return new PathPackResources(info, packPath);
            }
        };

        PackSelectionConfig selectionConfig = new PackSelectionConfig(true, Pack.Position.TOP, true);

        Pack pack = Pack.readMetaAndCreate(location, resources, PackType.CLIENT_RESOURCES, selectionConfig);
        if (pack != null) {
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }
}