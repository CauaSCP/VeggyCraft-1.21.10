package net.klayil.veggycraft.recipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.item.ModItems;
import net.klayil.veggycraft.recipe.piston_smash.PistonSmashRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@JeiPlugin
public class JeiClientPlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new AutoMixingCategoryJei(
                registration.getJeiHelpers().getGuiHelper()
        ));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(AutoMixingCategoryJei.PISTON_SMASH_TYPE, List.of(
                new PistonSmashRecipe(Ingredient.of(Items.WHEAT), new ItemStack(ModItems.THIS_MOD_FLOUR))
        ));
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(AutoMixingCategoryJei.PISTON_SMASH_TYPE, new ItemStack(Blocks.PISTON));
    }
}
