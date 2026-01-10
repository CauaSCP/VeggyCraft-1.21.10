package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.datagen.tags.ModBlockTagsProvider;
import net.klayil.veggycraft.datagen.tags.ModItemTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EventBusSubscriber(modid = VeggyCraft.MOD_ID)
public class ForgedDataGeneration {
    /*    ADD VeggyModModelProvider RUNNING HERE    */

    private static boolean pathExists(String path) {
        final Path pathVar = Paths.get(path);
        return Files.exists(pathVar) & Files.isDirectory(pathVar);
    }

    private static boolean deleteDir(String path) {
        File dir = new File(path);

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) { // listFiles() can return null on I/O error
                for (final File file : files) {
                    deleteDir(file.getPath()); // Recursive call for subdirectories
                }
            }
        }
        // Delete the file or now-empty directory
        return dir.delete();
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        final VeggyModModelProvider modelProvider = new VeggyModModelProvider(packOutput);

        String workingDir = Paths.get("").toAbsolutePath().toString();

        for (int ignoredI = 0; ignoredI < 2; ignoredI++) {
            workingDir = workingDir.substring(0, workingDir.lastIndexOf("/"));
        }

        String generatedFolderPath = "%s/common/src/generated".formatted(workingDir);

        if (pathExists(generatedFolderPath)) {
            try {

                deleteDir(generatedFolderPath);
            } catch (Exception e) {
                VeggyCraft.LOGGER.info("#DelError");
            }

            VeggyCraft.LOGGER.info("#WAITING...");

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                VeggyCraft.LOGGER.warn("Wait files deleting interrupted");
            }
        }


        event.addProvider(
            modelProvider
        );

        event.addProvider(
            new VeggyModRecipeProviderNeoForge.Runner(packOutput, lookupProvider)
        );

        event.addProvider(
            new ModItemTagsProvider(packOutput, Registries.ITEM, lookupProvider)
        );

        event.addProvider(
            new ModBlockTagsProvider(packOutput, Registries.BLOCK, lookupProvider)
        );

        generator.addProvider(true, new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        VeggyCraft.LOGGER.info("#DONE: CLIENT | %s | %s".formatted(lookupProvider.toString(), modelProvider.toString()));

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task =  () -> { if (pathExists(generatedFolderPath)) System.exit(0); };

        scheduler.scheduleAtFixedRate(task, 0, 3, TimeUnit.SECONDS);
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        VeggyCraft.LOGGER.info("#DONE: SERVER | "+lookupProvider.toString());
    }
}
