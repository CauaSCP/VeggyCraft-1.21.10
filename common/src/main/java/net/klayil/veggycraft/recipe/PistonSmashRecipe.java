package net.klayil.veggycraft.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public record PistonSmashRecipe(Ingredient inputItem, ItemStack output) implements Recipe<PistonSmashRecipeInput> {
    // inputItem & output ==> Read From JSON File!
    // PistonSmashRecipeInput --> INVENTORY of the Block Entity

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(inputItem);
        return list;
    }

    @Override
    public boolean matches(PistonSmashRecipeInput pistonSmashRecipeInput, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        return inputItem.test(pistonSmashRecipeInput.getItem(0));
    }

    @Override
    public ItemStack assemble(PistonSmashRecipeInput pistonSmashRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public RecipeSerializer<? extends Recipe<PistonSmashRecipeInput>> getSerializer() {
        return ModRecipes.PISTON_SMASH_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<PistonSmashRecipeInput>> getType() {
        return ModRecipes.PISTON_SMASH_TYPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(inputItem);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }

    public static class Serializer implements RecipeSerializer<PistonSmashRecipe> {
        public static final MapCodec<PistonSmashRecipe> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(PistonSmashRecipe::inputItem),
                ItemStack.CODEC.fieldOf("result").forGetter(PistonSmashRecipe::output)
        ).apply(inst, PistonSmashRecipe::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, PistonSmashRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, PistonSmashRecipe::inputItem,
                        ItemStack.STREAM_CODEC, PistonSmashRecipe::output,
                        PistonSmashRecipe::new);

        @Override
        public MapCodec<PistonSmashRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PistonSmashRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}