package net.klayil.veggycraft.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RotatedPillarBlock;

public class PalmLogBlock extends RotatedPillarBlock {


    public PalmLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(AXIS, Direction.Axis.Y));
    }
}
