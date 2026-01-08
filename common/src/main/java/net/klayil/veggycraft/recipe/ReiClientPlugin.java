package net.klayil.veggycraft.recipe;


import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.REIRuntime;
import me.shedaniel.rei.api.client.gui.widgets.TextField;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.display.reason.DisplayAdditionReason;
import me.shedaniel.rei.api.client.registry.display.reason.DisplayAdditionReasons;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
//import org.jetbrains.annotations.NotNull;

public class ReiClientPlugin implements REIClientPlugin {
//    public static final CategoryIdentifier<MysteriousItemConversionDisplay> MYSTERY_CONVERSION = CategoryIdentifier.of(MOD_ID, "mystery_conversion");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new AutoMixingCategory(), config(Items.WHEAT.asItem()));


//        registry.addWorkstations(AutoMixingCategory.PISTON_SMASH, EntryStacks.of(Blocks.PISTON_HEAD));
        registry.addWorkstations(AutoMixingCategory.PISTON_SMASH, EntryStacks.of(Blocks.PISTON));
    }
//
//    @SuppressWarnings("unchecked")
//    private <T extends Display> @NotNull Consumer<CategoryConfiguration<T>> config(ItemLike... item) {
//        EntryStack<ItemStack>[] workstations = new EntryStack[item.length];
//        for (int i = 0; i < item.length; i++) {
//            workstations[i] = EntryStacks.of(item[i]);
//        }
//        return config -> {
//            if (workstations.length > 0) {
//                config.addWorkstations(workstations);
//            }
//            config.setPlusButtonArea(bounds -> new Rectangle(bounds.getMaxX() - 16, bounds.getMinY() + 6, 10, 10));
//        };
//    }

    <T extends Display> Consumer<CategoryRegistry.CategoryConfiguration<T>> config(ItemLike... item) {
        EntryStack<ItemStack>[] workstations = new EntryStack[item.length];
        for (int i = 0; i < item.length; i++) {
            workstations[i] = EntryStacks.of(item[i]);
        }
        return config -> {
            if (workstations.length > 0) {
                config.addWorkstations(workstations);
            }
            config.setPlusButtonArea(bounds -> new Rectangle(bounds.getMaxX() - 16, bounds.getMinY() + 6, 10, 10));
        };
    }


    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.add(new MetaDisplay(
                (ShapelessCraftingRecipeDisplay) new ShapelessRecipe("smash", CraftingBookCategory.REDSTONE, new ItemStack(ModItems.THIS_MOD_FLOUR), List.of(Ingredient.of(Items.WHEAT))).display().getFirst()
        ));

//        reg = registry;
//        registry.beginFiller(PistonSmashRecipe.class);
//        registry.tryFillDisplay(ModRecipes.PISTON_SMASH_TYPE);

//        PistonSmashRecipe.get

//        registry.registerDisplaysFillerWithReason((o, r) -> this.predicate == null || this.predicate.test(o, r), (o, r) -> filler.apply((RecipeDisplay)o, Optional.ofNullable((DisplayAdditionReason.WithId)r.get(DisplayAdditionReason.WithId.class)).map(DisplayAdditionReason.WithId::id)))
//        registry.f

//        registry.addWithReason(ModRecipes.PISTON_SMASH_TYPE, DisplayAdditionReason.simple());
//

//        registry.fill

//        registry.add(new MysteriousItemConversionDisplay(AllItems.EMPTY_BLAZE_BURNER, AllItems.BLAZE_BURNER));
//        registry.add(new MysteriousItemConversionDisplay(AllItems.PECULIAR_BELL, AllItems.HAUNTED_BELL));
//        registerToolboxRecipes(registry);
//        EntryRegistry entrys = EntryRegistry.getInstance();
//        SpoutFillingDisplay.register(
//                entrys.getEntryStacks().filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.ITEM)),
//                entrys.getEntryStacks().filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.FLUID)),
//                registry
//        );
//        DrainingDisplay.register(entrys.getEntryStacks().filter(stack -> Objects.equals(stack.getType(), VanillaEntryTypes.ITEM)), registry);
    }

//    private static void registerToolboxRecipes(DisplayRegistry registry) {
//        EntryIngredient ingredient = EntryIngredients.of(Blocks.PISTON);
////        for (DyeColor color : DyeColor.values()) {
////            registry.add(new ClientsidedCraftingDisplay.Shapeless(
////                    List.of(ingredient, EntryIngredients.of(DyeItem.byColor(color))),
////                    List.of(EntryIngredients.of(ToolboxBlock.getColorBlock(color))),
////                    Optional.empty()
////            ));
////        }
//    }

    public static void setSearchField(String text) {
        TextField search = REIRuntime.getInstance().getSearchTextField();
        if (search != null) {
            search.setText(text);
        }
    }
}
