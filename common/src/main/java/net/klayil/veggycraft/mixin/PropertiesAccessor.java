package net.klayil.veggycraft.mixin;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(BlockBehaviour.Properties.class)
public interface PropertiesAccessor {
    @Accessor("mapColor")
    Function<BlockState, MapColor> getMapColor();

    @Accessor("mapColor")
    void setMapColor(Function<BlockState, MapColor> mapColor);
}
