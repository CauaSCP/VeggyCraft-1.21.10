package net.klayil.veggycraft.recipe;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.DeferredSupplier;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.klayil.veggycraft.recipe.wait_recipe.WaitRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;


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


    public static final DeferredSupplier<RecipeType<WaitRecipe>> WAIT_TYPE_RECIPE =
            TYPES.register("do_wait", () -> new RecipeType<WaitRecipe>() {
                @Override
                public String toString() {
                    return "do_wait";
                }
            });
    public static final DeferredSupplier<RecipeSerializer<WaitRecipe>> WAIT_SERIAL_RECIPE =
            SERIALIZERS.register("do_wait", WaitRecipe.Serializer::new);

    public static void register() {
        SERIALIZERS.register();
        TYPES.register();
    }
}