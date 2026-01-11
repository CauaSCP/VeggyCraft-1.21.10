package net.klayil.veggycraft.neoforge.mixin;

import net.klayil.veggycraft.block.HoneyBlockClone;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(HoneyBlockClone.class)
public class HoneyBlockCloneMixin implements IBlockExtension {

    @Override
    public boolean isStickyBlock(BlockState state) {
        if (state.getBlock() instanceof HoneyBlockClone) return true;

        return state.is(Blocks.SLIME_BLOCK) | state.is(Blocks.HONEY_BLOCK);
    }

    @Override
    public boolean canStickTo(@NotNull BlockState state, @NotNull BlockState other) {
        if (!canStickToEachOtherHelper(state, other)) return false;
        if (!canStickToEachOtherHelper(other, state)) return false;

        if (state.getBlock() == Blocks.HONEY_BLOCK && other.getBlock() == Blocks.SLIME_BLOCK) return false;
        if (state.getBlock() == Blocks.SLIME_BLOCK && other.getBlock() == Blocks.HONEY_BLOCK) return false;
        return state.isStickyBlock() || other.isStickyBlock();
    }

    @Unique
    private boolean canStickToEachOtherHelper(BlockState state1, BlockState state2) {
        return !(state1.getBlock() instanceof HoneyBlockClone & (state2.is(Blocks.HONEY_BLOCK)) | state2.is(Blocks.SLIME_BLOCK));
    }
}
