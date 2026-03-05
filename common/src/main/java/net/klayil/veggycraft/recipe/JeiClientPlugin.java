package net.klayil.veggycraft.recipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.item.MolassesToBrownSugar;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.klayil.veggycraft.recipe.wait_recipe.WaitRecipe;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@JeiPlugin
public class JeiClientPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new MixedCategoriesJei<>(
                MixedCategoriesJei.WAIT_TYPE_RECIPE,
                registration.getJeiHelpers().getGuiHelper(),
                MolassesToBrownSugar.parsedTexts
        ));

        registration.addRecipeCategories(new MixedCategoriesJei<>(
                MixedCategoriesJei.PISTON_SMASH_TYPE,
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ArrayList<IJeiBrewingRecipe> myBrewingRecipes = new ArrayList<>();

        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();

        ItemStack waterBottle = new ItemStack(Items.POTION);
        waterBottle.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.WATER));

        VeggyCraft.myRecipesStacks.add(
                Map.of(
                        new Item[] {
                                ModItems.OTHER_SPLASH_POTION.get(),
                                Items.SUGAR
                        },

                        ModItems.MUNDANE_SPLASH_POTION_ITEM_STACKS.getFirst()
                )
        );

        VeggyCraft.myRecipesStacks.add(
                Map.of(
                        new Item[] {
                                ModItems.ALGAE_EXTRACT.get(),
                                Items.KELP
                        },

                        waterBottle
                )
        );

        for (Map<Item[], ItemStack> toGetKeys : Objects.requireNonNull(VeggyCraft.myRecipesStacks)) {
            VeggyCraft.LOGGER.warn("#SIZ: %s", VeggyCraft.myRecipesStacks.size());

            for (Item[] recipeStuff : toGetKeys.keySet()) {
                Optional<Holder<Potion>> potionHolderOptional = Objects.requireNonNull(
                        toGetKeys.get(recipeStuff).get(DataComponents.POTION_CONTENTS)
            ).potion();

                String recipeUID = "%s_potion.to.%s".formatted(
                        potionHolderOptional.orElseThrow(() -> new NoSuchElementException("Couldn't parse the `%s` potion holder".formatted(Objects.requireNonNull(toGetKeys.get(recipeStuff).get(DataComponents.POTION_CONTENTS)).customName()))).value().name(),
                        BuiltInRegistries.ITEM.getKey(recipeStuff[0]).getPath()
                );

                VeggyCraft.LOGGER.warn("#recipeUID: %s", recipeUID);

                IJeiBrewingRecipe recipe = factory.createBrewingRecipe(
                        List.of(new ItemStack(recipeStuff[1])),
                        toGetKeys.get(recipeStuff),
                        new ItemStack(recipeStuff[0]),
                        ResourceLocation.fromNamespaceAndPath(
                                VeggyCraft.MOD_ID,
                                recipeUID
                        )
                );

                myBrewingRecipes.add(recipe);
            }
        }

        registration.addRecipes(RecipeTypes.BREWING, myBrewingRecipes);

        registration.addRecipes(MixedCategoriesJei.WAIT_TYPE_RECIPE, List.of(
                new WaitRecipe(Ingredient.of(ModItems.MOLASSES_BOTTLE.get()), new ItemStack(ModItems.DRIED_MOLASSES))
        ));

        registration.addRecipes(MixedCategoriesJei.PISTON_SMASH_TYPE, List.of(
                new PistonSmashRecipe(Ingredient.of(Items.WHEAT), new ItemStack(ModItems.THIS_MOD_FLOUR))
        ));
    }

/* *    @Override
//    public void registerIngredients(IModIngredientRegistration registration) {
//        registration.register(
//                VanillaTypes.ITEM_STACK,
//                List.of(new ItemStack(Items.CLOCK)),
//                new IIngredientHelper<>() {
//                    @Override
//                    public @NotNull IIngredientType<ItemStack> getIngredientType() {
//                        return VanillaTypes.ITEM_STACK;
//                    }
//
//                    @Override
//                    public @NotNull String getDisplayName(ItemStack ingredient) {
//                        return ingredient.getHoverName().getString();
//                    }
//
//                    @Override
//                    public @NotNull Object getUid(ItemStack ingredient, UidContext context) {
//                        ResourceLocation location = getResourceLocation(ingredient);
//
//                        return "%s:%s".formatted(location.getNamespace(), location.getPath());
//                    }
//
//                    @Override
//                    public @NotNull ResourceLocation getResourceLocation(ItemStack ingredient) {
//                        return BuiltInRegistries.ITEM.getKey(ingredient.getItem());
//                    }
//
//                    @Override
//                    public @NotNull ItemStack copyIngredient(ItemStack ingredient) {
//                        return ingredient.copy();
//                    }
//
//                    @Override
//                    public @NotNull String getErrorInfo(@Nullable ItemStack ingredient) {
//                        if (ingredient == null) return "null";
//                        return ingredient.toString();
//                    }
//                },
//                new ClockIngredientRenderer(),
//                ItemStack.CODEC
//        );
//    }
 */

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(MixedCategoriesJei.WAIT_TYPE_RECIPE, new ItemStack(ModItems.THIS_MOD_CLOCK));

        registration.addCraftingStation(MixedCategoriesJei.PISTON_SMASH_TYPE, new ItemStack(Blocks.PISTON));
    }
}
