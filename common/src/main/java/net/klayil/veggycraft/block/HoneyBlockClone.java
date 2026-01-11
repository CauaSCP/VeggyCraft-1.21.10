package net.klayil.veggycraft.block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HoneyBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HoneyBlockClone extends HoneyBlock {
    protected HoneyBlockClone(Properties properties) {
        super(properties.friction(0.8f)
                .speedFactor(0.4f)
                .jumpFactor(0.5f)
                .noOcclusion());
    }

    public static boolean isStickyHelper(BlockState state) {
        if (state.getBlock() instanceof HoneyBlockClone) return true;

        return state.is(Blocks.SLIME_BLOCK) | state.is(Blocks.HONEY_BLOCK);
    }

    public static boolean canStickToEachOtherHelper(BlockState state1, BlockState state2) {
        if ((state1.is(Blocks.HONEY_BLOCK) | state1.is(Blocks.SLIME_BLOCK)) & state2.getBlock() instanceof HoneyBlockClone) {
            return false;
        }

        if ((state2.is(Blocks.HONEY_BLOCK) | state2.is(Blocks.SLIME_BLOCK)) & state1.getBlock() instanceof HoneyBlockClone) {
            return false;
        }

        return isStickyHelper(state1) | isStickyHelper(state2);
    }
}
