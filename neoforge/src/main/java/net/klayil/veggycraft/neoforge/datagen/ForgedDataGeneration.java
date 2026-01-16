package net.klayil.veggycraft.neoforge.datagen;

import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.datagen.tags.ModBlockTagsProvider;
import net.klayil.veggycraft.datagen.tags.ModItemTagsProvider;
import net.klayil.veggycraft.world.ModConfiguredFeatures;
import net.klayil.veggycraft.world.ModPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.xml.crypto.Data;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@EventBusSubscriber(modid = VeggyCraft.MOD_ID)
public class ForgedDataGeneration {
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


        List<String> locales = List.of("af_za", "ar_sa", "ast_es", "az_az", "ba_ru", "bar", "be_by", "be_latn", "bg_bg", "br_fr", "brb", "bs_ba", "ca_es", "cs_cz", "cy_gb", "da_dk", "de_at", "de_ch", "de_de", "el_gr", "en_au", "en_ca", "en_gb", "en_nz", "en_pt", "en_ud", "en_us", "enp", "enws", "eo_uy", "es_ar", "es_cl", "es_ec", "es_es", "es_mx", "es_uy", "es_ve", "esan", "et_ee", "eu_es", "fa_ir", "fi_fi", "fil_ph", "fo_fo", "fr_ca", "fr_fr", "fra_de", "fur_it", "fy_nl", "ga_ie", "gd_gb", "gl_es", "hal_ua", "haw_us", "he_il", "hi_in", "hn_no", "hr_hr", "hu_hu", "hy_am", "id_id", "ig_ng", "io_en", "is_is", "isv", "it_it", "ja_jp", "jbo_en", "ka_ge", "kk_kz", "kn_in", "ko_kr", "ksh", "kw_gb", "ky_kg", "la_la", "lb_lu", "li_li", "lmo", "lo_la", "lol_us", "lt_lt", "lv_lv", "lzh", "mk_mk", "mn_mn", "ms_my", "mt_mt", "nah", "nds_de", "nl_be", "nl_nl", "nn_no", "no_no", "oc_fr", "ovd", "pl_pl", "pls", "pt_br", "pt_pt", "qcb_es", "qid", "qya_aa", "ro_ro", "rpr", "ru_ru", "ry_ua", "sah_sah", "se_no", "sk_sk", "sl_si", "so_so", "sq_al", "sr_cs", "sr_sp", "sv_se", "sxu", "szl", "ta_in", "th_th", "tl_ph", "tlh_aa", "tok", "tr_tr", "tt_ru", "tzo_mx", "uk_ua", "val_es", "vec_it", "vi_vn", "vp_vl", "yi_de", "yo_ng", "zh_cn", "zh_hk", "zh_tw", "zlm_arab");

        for (String locale : locales) {
            generator.addProvider(
                    true,
                    new MinecraftLanguageProvider(packOutput, locale)
            );

            if (locale.toLowerCase().startsWith("en_") | locale.toLowerCase().startsWith("pt_")) generator.addProvider(
                    true,
                    new ModLanguageProvider(packOutput, locale)
            );
        }

        RegistrySetBuilder builder = new RegistrySetBuilder()
                .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
        event.createDatapackRegistryObjects(builder, Set.of(VeggyCraft.MOD_ID));

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
//        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        VeggyCraft.LOGGER.info("#DONE: SERVER | "+lookupProvider.toString());
    }
}
