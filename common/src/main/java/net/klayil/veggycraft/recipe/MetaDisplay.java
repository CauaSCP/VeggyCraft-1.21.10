package net.klayil.veggycraft.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetaDisplay extends BasicDisplay {
    public MetaDisplay(ShapelessCraftingRecipeDisplay recipe) {
        super(recipe.ingredients().stream()
                        .map(EntryIngredients::ofSlotDisplay)
                        .toList(),
                // Extracting the result from the display logic
                List.of(EntryIngredients.ofSlotDisplay(recipe.result())));
    }

    public MetaDisplay(List<EntryIngredient> list, List<EntryIngredient> es) {
        super(list, es);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return AutoMixingCategory.PISTON_SMASH;
    }

    public static final Codec<MetaDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(BasicDisplay::getInputEntries),
            EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BasicDisplay::getOutputEntries)
    ).apply(instance, MetaDisplay::new));

    // 2. The StreamCodec (Network)
    public static final StreamCodec<RegistryFriendlyByteBuf, MetaDisplay> STREAM_CODEC = StreamCodec.composite(
            EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), BasicDisplay::getInputEntries,
            EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), BasicDisplay::getOutputEntries,
            MetaDisplay::new
    );

    @Override
    public @Nullable DisplaySerializer<? extends MetaDisplay> getSerializer() {
        return new DisplaySerializer() {
            @Override
            public MapCodec<MetaDisplay> codec() {
                return (MapCodec<MetaDisplay>) CODEC;
            }

            @Override
            public StreamCodec<RegistryFriendlyByteBuf, MetaDisplay> streamCodec() {
                return STREAM_CODEC;
            }

        };
    }
}
