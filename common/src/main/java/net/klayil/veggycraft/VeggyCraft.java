package net.klayil.veggycraft;

//import net.klayil.veggycraft.compat.recipe.ModRecipes;
import com.mojang.serialization.Lifecycle;
import dev.architectury.event.events.common.LifecycleEvent;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.compat.BedCompat;
import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;

import net.klayil.veggycraft.item.tabs.VeggyCraftTabsCode;
import net.klayil.veggycraft.mixin.AxeItemAccessor;
import net.klayil.veggycraft.mixin.FireBlockAccessor;
import net.klayil.veggycraft.platform.PlatformHelper;
import net.klayil.veggycraft.recipe.ModRecipes;
import net.klayil.veggycraft.tags.ModItemTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import net.minecraft.world.level.block.Block;

public final class VeggyCraft {
    public static final String MOD_ID = "veggycraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        ModRecipes.register();

        ModDataComponentTypes.registerDataComponentTypes();

        VeggyCraftTabsCode.initTabs();
        ModBlocks.initBlocks();
        ModItems.initItems();

        BedCompat.registerBed(ModBlocks.STRAW_BED.get());

        ModItemTags.init();

        VeggyCraftTabsCode.initAfterItems.run();

        LifecycleEvent.SETUP.register(
                VeggyCraft::setupTreeBlockStuff
        );
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
