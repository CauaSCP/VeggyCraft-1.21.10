package net.klayil.veggycraft.block.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class DeferredBlockBreaker {
    private static final Queue<BlockPos> QUEUE = new ConcurrentLinkedDeque<>();

    public static void requestBreak(BlockPos pos) {
        QUEUE.add(pos.immutable());
    }

    public static void process(ServerLevel level) {
        BlockPos pos;

        while ((pos = QUEUE.poll()) != null) {
            BlockState state = level.getBlockState(pos);
            if (!state.isAir()) {
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }
    }
}
