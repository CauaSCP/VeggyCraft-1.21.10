package net.klayil.veggycraft.world.carnauba.custom_code;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.klayil.veggycraft.mixin.FoliagePlacerTypeInvoker;

public class ModFoliagePlacers {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACERS = DeferredRegister.create(
            VeggyCraft.MOD_ID, Registries.FOLIAGE_PLACER_TYPE
    );

    public static final RegistrySupplier<FoliagePlacerType<FoliagePlacer>> PALM_TREE_PLACER = FOLIAGE_PLACERS.register(
            "palm_tree_like_foliage_placer", () -> FoliagePlacerTypeInvoker.callCreate(PalmTreeLikeFoliagePlacer.CODEC)
    );

    public static void init() {
        FOLIAGE_PLACERS.register();
    }
}
