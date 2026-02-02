package net.klayil.veggycraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PillaredFence extends RotatedPillarBlock {
    public PillaredFence(Properties properties) {
        super(properties);
    }

    protected static final VoxelShape Y_SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.001, 10.0);
    protected static final VoxelShape Z_SHAPE = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 16.001);
    protected static final VoxelShape X_SHAPE = Block.box(0.0, 6.0, 6.0, 16.001, 10.0, 10.0);

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(AXIS)) {
            case X -> { return X_SHAPE; }
            case Z -> { return Z_SHAPE; }
            default -> { return Y_SHAPE; }
        }
    }
}
