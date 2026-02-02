package net.klayil.veggycraft.recipe.wait_recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.klayil.veggycraft.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record WaitRecipe(Ingredient inputItem, ItemStack output) implements Recipe<WaitRecipeInput> {
    @Override
    public boolean matches(WaitRecipeInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(WaitRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<WaitRecipeInput>> getSerializer() {
        return ModRecipes.WAIT_SERIAL_RECIPE.get();
    }

    @Override
    public RecipeType<? extends Recipe<WaitRecipeInput>> getType() {
        return ModRecipes.WAIT_TYPE_RECIPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<WaitRecipe> {
        public static final MapCodec<WaitRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(WaitRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(WaitRecipe::output)
        ).apply(inst, WaitRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WaitRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, WaitRecipe::inputItem,
                        ItemStack.STREAM_CODEC, WaitRecipe::output,
                        WaitRecipe::new);

        @Override
        public MapCodec<WaitRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, WaitRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
