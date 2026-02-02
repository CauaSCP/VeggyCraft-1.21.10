package net.klayil.veggycraft;

//import net.klayil.veggycraft.compat.recipe.ModRecipes;
import com.mojang.serialization.Lifecycle;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import dev.architectury.utils.value.IntValue;
import net.klayil.FinalInfoLogger;
import net.klayil.veggycraft.block.ModBedBlock;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.compat.BedCompat;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;

import net.klayil.veggycraft.item.tabs.VeggyCraftTabsCode;
import net.klayil.veggycraft.mixin.AxeItemAccessor;
import net.klayil.veggycraft.mixin.FireBlockAccessor;
import net.klayil.veggycraft.platform.PlatformHelper;
import net.klayil.veggycraft.recipe.ModRecipes;
import net.klayil.veggycraft.tags.ModTags;
import net.klayil.veggycraft.world.carnauba.custom_code.ModFoliagePlacers;
import net.klayil.veggycraft.world.carnauba.custom_code.ModTrunkPlacers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import net.minecraft.world.level.block.Block;

public final class VeggyCraft {
    public static final String MOD_ID = "veggycraft";
    private static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    public static final FinalInfoLogger LOGGER = new FinalInfoLogger(logger);

    @Nullable
    public static ServerLevel level = null;

    public static void init() {
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

//        BlockEvent.BREAK.register(VeggyCraft::onBlockBreak); // Incompatible types: IntValue is not convertible to int

        LifecycleEvent.SETUP.register(
                VeggyCraft::setupTreeBlockStuff
        );

        TickEvent.SERVER_LEVEL_POST.register(l -> level = l);

        InteractionEvent.RIGHT_CLICK_BLOCK.register(VeggyCraft::evenBirchStripped);
    }

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

        level.setBlock(pos, ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get().defaultBlockState(), 11);
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

        for (String key : ModBlocks.CARNAUBA_WOODS.keySet()) {
            if (ModBlocks.CARNAUBA_WOODS.get(key) instanceof SaplingBlock) continue;

            List<String> keySplit = new ArrayList<>(Arrays.stream(key.split("_")).toList());
            keySplit.addFirst("");

            String prefix = keySplit.get(keySplit.size()-2);
            prefix = (Objects.equals(prefix, "")) ? "" : prefix+"_";

            BlockState state = BuiltInRegistries.BLOCK.getValue(ResourceLocation.withDefaultNamespace(
                    prefix+"jungle_"+keySplit.getLast()
            )).defaultBlockState();

            FireBlockAccessor accessor = ((FireBlockAccessor) fireBlock);

            Block curFlammable = ModBlocks.CARNAUBA_WOODS.get(key).getOrNull();

            if (curFlammable == null) continue;

            fireBlock.setFlammable(curFlammable, accessor.getEncouragement(state), accessor.getFlammabilityOdds(state));
        }
    }
}
