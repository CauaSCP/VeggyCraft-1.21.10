package net.klayil.veggycraft.compat;

import com.mojang.math.OctahedralGroup;
import com.mojang.math.Quadrant;

import net.minecraft.world.level.block.Block;
import net.klayil.veggycraft.mixin.BlockEntityTypeAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class BedCompat {
    private BedCompat() {}

    public static OctahedralGroup fromXYAngles(
            Quadrant x,
            Quadrant y
    ) {
        return MinecraftCompat.fromXYAngles(x, y);
    }

    public static void registerBed(Block bed) { // no usages
        BlockEntityTypeAccessor acc = (BlockEntityTypeAccessor) (Object) BlockEntityType.BED;
        acc.veggycraft$getValidBlocks().add(bed);
    }
}

/*
public final class BedCompat {
    private BedCompat() {}

    public static OctahedralGroup fromXYAngles(
            Quadrant x,
            Quadrant y
    ) {
        return MinecraftCompat.fromXYAngles(x, y);
    }

    public static void registerBed(Block bed) {
        BlockEntityTypeAccessor acc =
                (BlockEntityTypeAccessor) (Object) BlockEntityType.BED;

        acc.veggycraft$getValidBlocks().add(bed);
    }
}
*/
