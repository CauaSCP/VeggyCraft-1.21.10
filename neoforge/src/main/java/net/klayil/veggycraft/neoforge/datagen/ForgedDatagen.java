package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = VeggyCraft.MOD_ID)
public class ForgedDatagen {
    /*    ADD VeggyModModelProvider RUNNING HERE    */

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final VeggyModModelProvider modelProvider = new VeggyModModelProvider(packOutput);

        event.addProvider(
            modelProvider
        );

        event.addProvider(
            new VeggyModRecipeProviderNeoForge.Runner(packOutput, lookupProvider)
        );

        VeggyCraft.LOGGER.info("#DONE: CLIENT | %s | %s".formatted(lookupProvider.toString(), modelProvider.toString()));
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        VeggyCraft.LOGGER.info("#DONE: SERVER | "+lookupProvider.toString());
    }
}
