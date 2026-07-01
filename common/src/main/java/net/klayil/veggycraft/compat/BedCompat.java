package net.klayil.veggycraft.compat;

import net.minecraft.world.level.block.Block;
import net.klayil.veggycraft.mixin.BlockEntityTypeAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BedCompat {
    public static void registerBed(Block bed) {
        BlockEntityTypeAccessor acc = (BlockEntityTypeAccessor)(Object) BlockEntityType.BED;
        acc.veggycraft$getValidBlocks().add(bed);
    }
}
