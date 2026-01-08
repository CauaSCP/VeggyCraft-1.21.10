package net.klayil.veggycraft.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;


public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(VeggyCraft.MOD_ID, Registries.RECIPE_SERIALIZER);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(VeggyCraft.MOD_ID, Registries.RECIPE_TYPE);

    public static final DeferredSupplier<RecipeSerializer<PistonSmashRecipe>> PISTON_SMASH_SERIALIZER =
            SERIALIZERS.register("piston_smashin", PistonSmashRecipe.Serializer::new);
    public static final DeferredSupplier<RecipeType<PistonSmashRecipe>> PISTON_SMASH_TYPE =
            TYPES.register("piston_smashin", () -> new RecipeType<PistonSmashRecipe>() {
                @Override
                public String toString() {
                    return "piston_smashin";
                }
            });


    public static void register() {
        SERIALIZERS.register();
        TYPES.register();
    }
}