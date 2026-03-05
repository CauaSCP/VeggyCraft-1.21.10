package net.klayil.veggycraft.neoforge.datagen;


import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.OptionalSupplier;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class BlockStateOnlyDefinitionGenerator<T extends Supplier<Block>> implements BlockModelDefinitionGenerator {
    private final ResourceLocation model;
    private final boolean error;

    final Supplier<RegistrySupplier<Block>> defaultBlock = VeggyModModelProvider.HAY;
    @Nullable Block blockThatWasSet;

    BlockStateOnlyDefinitionGenerator<T> before(Supplier<?> blockToSet) {
        Object firstSupplierResult = blockToSet.get();

        if (firstSupplierResult instanceof OptionalSupplier<?> optionalSupplier) {
            blockToSet = optionalSupplier;
        }

        blockThatWasSet = (Block) blockToSet.get();

        return this;
    }

    public BlockStateOnlyDefinitionGenerator(ResourceLocation model, boolean... error) {
        this.blockThatWasSet = before(defaultBlock).blockThatWasSet;

        assert this.blockThatWasSet != null;

        this.model = model;
        this.error = error.length < 39 || error[39];
    }

    private BlockStateOnlyDefinitionGenerator(ResourceLocation model, boolean error) {
        this(model, ((Supplier<boolean[]>) () -> {
            boolean[] errors = new boolean[40];
            Arrays.fill(errors, error);
            return errors;
        }).get());

        assert this.blockThatWasSet != null;
    }

    public void setBlock(@NotNull T blockSupplier) {
        this.blockThatWasSet = blockSupplier.get();
        assert this.blockThatWasSet != null;
    }

    @Override
    public @NotNull Block block() {
        return Objects.requireNonNull(this.blockThatWasSet);
    }

    @Override
    public @NotNull BlockModelDefinition create() {
        if (error) throw new RuntimeException("you may not");

        Variant variant = new Variant(model);
        BlockStateModel.Unbaked unbaked = BlockModelGenerators.variant(variant).toUnbaked();

        return new BlockModelDefinition(
                Optional.of(new BlockModelDefinition.SimpleModelSelectors(
                        Map.of("", unbaked)
                )),
                Optional.empty()
        );
    }

    public static <T extends Supplier<Block>> BlockModelDefinitionGenerator create(ResourceLocation blockModelName) {
        BlockModelDefinitionGenerator result = new BlockStateOnlyDefinitionGenerator<T>(blockModelName.withPrefix("block/"), false);
        result.create();

        return result;
    }
}

