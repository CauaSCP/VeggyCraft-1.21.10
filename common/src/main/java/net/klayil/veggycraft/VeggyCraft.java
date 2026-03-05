package net.klayil.veggycraft;

import dev.architectury.event.events.common.*;
import dev.architectury.registry.ReloadListenerRegistry;
import lombok.Setter;
import lombok.SneakyThrows;
import net.klayil.FinalInfoLogger;
import net.klayil.MapToJson;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.image_creating.AnimatedExtendedBufferedImage;
import net.klayil.veggycraft.image_creating.ImageProperties;
import net.klayil.veggycraft.item.ModItems;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.Resource;
import net.klayil.veggycraft.item.potions.effect.ModEffects;
import net.klayil.veggycraft.item.tabs.VeggyCraftTabsCode;
import net.klayil.veggycraft.mixin.AxeItemAccessor;
import net.klayil.veggycraft.mixin.FireBlockAccessor;
import net.klayil.veggycraft.recipe.ModRecipes;
import net.klayil.veggycraft.tags.ModTags;
import net.klayil.veggycraft.world.carnauba.custom_code.ModFoliagePlacers;
import net.klayil.veggycraft.world.carnauba.custom_code.ModTrunkPlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public final class VeggyCraft {
    public static final String MOD_ID = "veggycraft";
    private static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    public static final FinalInfoLogger LOGGER = new FinalInfoLogger(logger);

    public static final String animatedClockResourcePack = "Animated Clock for Recipe Hints";

    public static SequencedSet<Map<Item[], ItemStack>> myRecipesStacks = new LinkedHashSet<>();

    @Nullable
    public static ServerLevel level = null;

    @Setter
    public static boolean metaDataGen;
    public static boolean isDataGen;

     public final static String workingDir = Paths.get("").toAbsolutePath().toString();
    private final static String packFilepath = "%s/resourcepacks/%s/pack".formatted(workingDir, animatedClockResourcePack);

    static boolean parsedErr = false;

    static File myClockFile = null;

    static int index;
    @Nullable static Graphics2D combinedGraphics;

//    public void comment(String... ignoreComments) {}
    public static void comment(String ignoreComment, String... ignoreComments) {}

    static void processResource(Resource resource, final int height) {
        try {
            InputStream inputStream = resource.open();

            BufferedImage currentImg = ImageIO.read(inputStream);

            int yOffset = index / 2 * height;

            assert combinedGraphics != null;

            combinedGraphics.drawImage(currentImg, 0, yOffset, null);
        } catch (IOException e) {
            VeggyCraft.LOGGER.error("#ERR: %s", e.getMessage());
        }
    }

    static void animationMcmeta(Resource resource) {
        try {
            BufferedReader resourceReader = resource.openAsReader();

            assert myClockFile != null;
            Files.writeString(Path.of("%s.mcmeta".formatted(myClockFile.getPath())), resourceReader.lines().collect(Collectors.joining("\n")));

            resourceReader.close();
        } catch (IOException e) {
            parseError(e);
        }
    }

    @Nullable static Integer animatedItemHeight;
    @SneakyThrows
    public static void imageGen(ResourceManager parsedManager) {
        int count = 64;
        final ImageProperties clockImageProperties = ImageProperties.init();

        try {
            myClockFile = new File("%s/resourcepacks/%s/assets/%s/textures/item/my_clock.png".formatted(
                    workingDir
                    ,
                    animatedClockResourcePack
                    ,
                    VeggyCraft.MOD_ID

            ));

            Path parentPath = Paths.get(myClockFile.getParent());
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
            }

            final AnimatedExtendedBufferedImage combinedImage = new AnimatedExtendedBufferedImage();
            for (index = 0; index < count; index += 2) {
                ResourceLocation clockImageFilePath = ResourceLocation.withDefaultNamespace("textures/item/clock_%02d.png".formatted(index));

                Optional<Resource> clockTextureResource = parsedManager.getResource(clockImageFilePath);

                if (index == 0) {
                    clockTextureResource.ifPresent(resource -> {
                        clockImageProperties.setProportions().accept(resource);
                        animatedItemHeight = clockImageProperties.getHeight();

                        try {
                            VeggyCraft.LOGGER.warn(">>proportions {Clock Image}: %s, %s", clockImageProperties.getWidth(), clockImageProperties.getHeight());

                            combinedImage.parseProportions(clockImageProperties);
                            combinedImage.extendProportions((p, ex) -> p * ex, null, (count / 2));

                            combinedGraphics = combinedImage.buffered().createGraphics();

                            final BufferedImage resourcePackIconBuffered = new BufferedImage(
                                    clockImageProperties.getWidth(),
                                    clockImageProperties.getHeight(),
                                    BufferedImage.TYPE_INT_ARGB
                            );

                            resourcePackIconBuffered.createGraphics().drawImage(ImageIO.read(resource.open()), 0, 0, null);

                            ImageIO.write(
                                    resourcePackIconBuffered,
                                    "PNG",
                                    new File("%s.png".formatted(packFilepath))
                            );
                        } catch (IOException e) {
                            parseError(e);
                        }
                    });

                    if (parsedErr) return;
                }

                assert animatedItemHeight != null;
                clockTextureResource.ifPresent(res -> processResource(res, animatedItemHeight));
            }

            assert combinedGraphics != null;
            combinedGraphics.dispose();

//            VeggyCraft.LOGGER.warn("#WorkingDirectory: %s", workingDir);

            ResourceLocation myClockMcmeta = ResourceLocation.fromNamespaceAndPath(
                    VeggyCraft.MOD_ID,
                    "textures/item/my_clock.png.mcmeta"
            );

            parsedManager.getResource(myClockMcmeta).ifPresent(VeggyCraft::animationMcmeta);

            if (parsedErr) return;

//            VeggyCraft.LOGGER.warn("#Deleted: %s", file.delete());

            ImageIO.write(
                    combinedImage.buffered(),
         "PNG",
                    myClockFile
            );

            new MapToJson<>(
                Map.of(
                "pack", Map.of(
                        "description", "",
                        "min_format", 69,
                        "max_format", 69,
                        "pack_format", 69
                    )
                ),

                "%s.mcmeta".formatted(packFilepath)
            );

//            VeggyCraft.LOGGER.warn("#FileExists: %s | %s", f.exists(), f.getPath());
        } catch (IOException e) {
            parseError(e);
        }
    }

    static void parseError(Exception e) {
        parsedErr = true;
        VeggyCraft.LOGGER.error("#ERR: %s", e.getMessage());
    }

    public static void enableResourcePack(Minecraft parsedInstance, String packName) {
//        File source = new File("%s/../src/main/resources/resourcepacks/veggycraft_overrides/".formatted(
//                workingDir
//        ));
//
//        File destination = new File("%s/resourcepacks/%s".formatted(workingDir));

//        try {
////            FileUtils.copyDirectory(source, destination);
//        } catch (IOException e) {
//            VeggyCraft.LOGGER.error("#ERR: %s", e.getMessage());
//        }

//        File hereSource = new File("%s/resourcepacks/%s__".formatted(workingDir));
//
//        File hereDestination = new File("%s/resourcepacks/%s".formatted(workingDir));
//
//        try {
//            FileUtils.copyDirectory(hereSource, hereDestination);
////                Files.move(Path.of(hereSource.toURI()), Path.of(hereDestination.toURI()), StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            VeggyCraft.LOGGER.error("#ERR: %s", e.getMessage());
//        }

        PackRepository repo = parsedInstance.getResourcePackRepository();
        repo.reload();
        String packId = "file/%s".formatted(packName);

        assert repo.getPack(packId) != null;

        ArrayList<String> selectedIds = new ArrayList<>(repo.getSelectedIds());

        if (!selectedIds.contains(packId)) {
            selectedIds.add(packId);

            repo.setSelected(selectedIds);

            parsedInstance.options.resourcePacks.clear();
            parsedInstance.options.resourcePacks.addAll(repo.getSelectedIds());

            parsedInstance.options.save();

            parsedInstance.reloadResourcePacks();

/* *            if (doReload) {
//                doReload = false;
//            }
*/
        }
    }

//    private static boolean doReload = true;

    public static void init() {
        VeggyCraft.LOGGER.warn("#INIT, %s", metaDataGen);

        isDataGen = metaDataGen;

        ModRecipes.register();

        ModDataComponentTypes.registerDataComponentTypes();

        VeggyCraftTabsCode.initTabs();
        ModBlocks.initBlocks();
        ModItems.initItems();

        ModFoliagePlacers.init();
        ModTrunkPlacers.init();

//        BedCompat.registerBed(ModBlocks.STRAW_BED.get());
        ModTags.init();

        VeggyCraftTabsCode.initAfterItems.run();

        ModEffects.init();

//        BlockEvent.BREAK.register(VeggyCraft::onBlockBreak); // Incompatible types: IntValue is not convertible to int

        LifecycleEvent.SETUP.register(
                VeggyCraft::setupTreeBlockStuff
        );

        TickEvent.SERVER_LEVEL_POST.register(l -> level = l);

        InteractionEvent.RIGHT_CLICK_BLOCK.register(VeggyCraft::evenBirchStripped);

        ReloadListenerRegistry.register(
                PackType.CLIENT_RESOURCES,
                new ResourcePacksReloadListener(),
                ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "resource_listener")
        );

        comment("""
//    private static EventResult onBlockBreak(Level level, BlockPos pos, BlockState state, Player player, IntValue xp) {
//        if (!(state.getBlock() instanceof ModBedBlock)) return EventResult.pass();
//
//        if (!level.isClientSide()) {
//            BedPart part = state.getValue(ModBedBlock.PART);
//            Direction facing = state.getValue(ModBedBlock.FACING);
//
//            BlockPos otherPos = pos.relative(
//                    ModBedBlock.getNeighbourDirection(part, facing)
//            );
//
//            level.setBlock(otherPos, Blocks.AIR.defaultBlockState(),
//                    Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS | Block.UPDATE_SUPPRESS_DROPS
//            );
//
//            level.setBlock(pos, Blocks.AIR.defaultBlockState(),
//                    Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS | Block.UPDATE_SUPPRESS_DROPS
//            );
//        }
//
//        return EventResult.interruptFalse();
//    }
""");
    }

    private static InteractionResult evenBirchStripped(
            Player player,
            InteractionHand hand,
            BlockPos pos,
            Direction face
    ) {
        Level level = Objects.requireNonNull(player.level());

        ItemStack stack = player.getItemInHand(hand);

        if (!(stack.getItem() instanceof AxeItem)) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);

        if (!(state.is(Blocks.STRIPPED_BIRCH_LOG))) {
            return InteractionResult.PASS;
        }

        BlockState evensState = ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get().withPropertiesOf(state);

        level.setBlock(pos, evensState, 11);

        stack.hurtAndBreak(1, player, hand);
        level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1F, 1F);

        return InteractionResult.SUCCESS;
    }

    public static void setupTreeBlockStuff() {
        HashMap<Block, Block> strippables = new HashMap<>(
                AxeItemAccessor.getStrippables()
        );

        Block wood = ModBlocks.CARNAUBA_WOODS.get("wood").getOrNull();
        Block log = ModBlocks.CARNAUBA_WOODS.get("log").getOrNull();

        if (wood != null & log != null) {
            strippables.put(wood, ModBlocks.CARNAUBA_WOODS.get("stripped_wood").get());
            strippables.put(log, ModBlocks.CARNAUBA_WOODS.get("stripped_log").get());
        }

        AxeItemAccessor.setStrippables(strippables);

        FireBlock fireBlock = (FireBlock) Blocks.FIRE;
        FireBlockAccessor accessor = ((FireBlockAccessor) fireBlock);

        for (String key : ModBlocks.CARNAUBA_WOODS.keySet()) {
            if (ModBlocks.CARNAUBA_WOODS.get(key) instanceof SaplingBlock) continue;

            List<String> keySplit = new ArrayList<>(Arrays.stream(key.split("_")).toList());
            keySplit.addFirst("");

            String prefix = keySplit.get(keySplit.size()-2);
            prefix = (Objects.equals(prefix, "")) ? "" : prefix+"_";

            BlockState state = BuiltInRegistries.BLOCK.getValue(ResourceLocation.withDefaultNamespace(
                    prefix+"jungle_"+keySplit.getLast()
            )).defaultBlockState();


            Block curFlammable = ModBlocks.CARNAUBA_WOODS.get(key).getOrNull();

            if (curFlammable == null) continue;

            fireBlock.setFlammable(curFlammable, accessor.igniteOdds(state), accessor.burnOdds(state));
        }

        BlockState birchState = Blocks.STRIPPED_BIRCH_LOG.defaultBlockState();
        fireBlock.setFlammable(ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get(), accessor.igniteOdds(birchState), accessor.burnOdds(birchState));
    }
}
