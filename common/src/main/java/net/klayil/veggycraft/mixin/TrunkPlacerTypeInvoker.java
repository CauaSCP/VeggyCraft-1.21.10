package net.klayil.veggycraft.mixin;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

//@Mixin(FoliagePlacerType.class)
//public interface FoliagePlacerTypeInvoker {
//    @Invoker("<init>")
//    static <P extends FoliagePlacer> FoliagePlacerType<P> callCreate(MapCodec<P> codec) {
//        throw new UnsupportedOperationException();
//    }
//}

@Mixin(TrunkPlacerType.class)
public interface TrunkPlacerTypeInvoker {
    @Invoker("<init>")
    static <P extends TrunkPlacer> TrunkPlacerType<P> callCreate(MapCodec<P> codec) {
        throw new UnsupportedOperationException();
    }
}
