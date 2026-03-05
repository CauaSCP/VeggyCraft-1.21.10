package net.klayil.veggycraft.recipe.wait_recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.klayil.veggycraft.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public record WaitRecipe(Ingredient inputItem, ItemStack output) implements Recipe<WaitRecipeInput> {
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }


    public ItemStack getResultItem(HolderLookup.Provider ignoredParameter) {

        return output.copy();

    }

    @Override
    public boolean matches(WaitRecipeInput input, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(input.getItem(0));
    }

    @Override
    public @NotNull ItemStack assemble(WaitRecipeInput input, HolderLookup.Provider registries) {
        return output.copy();
    }

    @Override
    public @NotNull RecipeSerializer<? extends Recipe<WaitRecipeInput>> getSerializer() {
        return ModRecipes.WAIT_SERIAL_RECIPE.get();
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<WaitRecipeInput>> getType() {
        return ModRecipes.WAIT_TYPE_RECIPE.get();
    }

    @Override
    public @NotNull PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<WaitRecipe> {
        public static final MapCodec<WaitRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(WaitRecipe::inputItem),
                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(WaitRecipe::output)
        ).apply(inst, WaitRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, WaitRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, WaitRecipe::inputItem,
                        ItemStack.STREAM_CODEC, WaitRecipe::output,
                        WaitRecipe::new);

        @Override
        public @NotNull MapCodec<WaitRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, WaitRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}