package net.klayil.veggycraft.world.carnauba.custom_code;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.mixin.TrunkPlacerTypeInvoker;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

public class ModTrunkPlacers {
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACERS = DeferredRegister.create(
            VeggyCraft.MOD_ID, Registries.TRUNK_PLACER_TYPE
    );

    public static final RegistrySupplier<TrunkPlacerType<TrunkPlacer>> CARNAUBA_TRUNK = TRUNK_PLACERS.register(
            "carnauba_trunk", () -> TrunkPlacerTypeInvoker.callCreate((MapCodec<TrunkPlacer>) ((MapCodec) CarnaubaTrunkPlacer.CODEC))
    );

    public static void init() {
        TRUNK_PLACERS.register();
    }
}
