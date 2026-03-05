package net.klayil.veggycraft.mixin;

import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {

    @Invoker("getIgniteOdds")
    int igniteOdds(BlockState state);

    @Invoker("getBurnOdds")
    int burnOdds(BlockState state);
}
