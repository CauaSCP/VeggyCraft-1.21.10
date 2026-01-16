package net.klayil.veggycraft.datagen.recipe;

import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.veggycraft.VeggyCraft;

import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.datagen.ColoursList;
import net.klayil.veggycraft.tags.ModItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Recipe;

import net.klayil.veggycraft.component.ModDataComponentTypes;
import net.klayil.veggycraft.item.ModItems;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.*;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.advancements.Criterion;

//import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;

public class VeggyModRecipeProviderCommon extends RecipeProvider {
    @Nullable
    private HolderGetter.Provider holderGetterProvider = null;

    public VeggyModRecipeProviderCommon(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
        this.holderGetterProvider = provider;
    }

    public static class Runner extends RecipeProvider.Runner{
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected @NotNull RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new VeggyModRecipeProviderCommon(provider, recipeOutput);
        }
        @Override
        public @NotNull String getName() { return "My Recipes"; }

    }

    public ShapedRecipeBuilder shapedLocal(RecipeCategory category, ItemStack result) {
        HolderGetter<Item> itemLookup = this.registries.lookupOrThrow(Registries.ITEM);

        return ShapedRecipeBuilder.shaped(itemLookup, category, result);
    }

    protected ItemStack flourBagSize(int itemSizeAtBeginNum) {
//        final String itemName = "%02d_items_stacked_of_flour".formatted(itemSizeAtBeginNum);

        ItemStack to_return = new ItemStack(ModItems.FLOUR_BAG);

        DataComponentType<ModDataComponentTypes.ItemHealth> HEALTH =
                ModDataComponentTypes.HEALTH.get();

        ModDataComponentTypes.ItemHealth old =
                to_return.getOrDefault(HEALTH, new ModDataComponentTypes.ItemHealth(0, itemSizeAtBeginNum));


        to_return.set(
                HEALTH,
                new ModDataComponentTypes.ItemHealth(itemSizeAtBeginNum, old.max())
        );

        return to_return;
    }

    public ShapedRecipeBuilder flourRecipe(int resultItemSize) {
        Item flour = ModItems.THIS_MOD_FLOUR.get();
        ItemStack result = flourBagSize(resultItemSize);

        ShapedRecipeBuilder ret = shapedLocal(RecipeCategory.MISC, result);

        ret = ret
                .pattern("fff")
                .pattern("fBf")
                .pattern("fff")
                .define('f', flour)
                .define('B', ComponentIngredientOf(flourBagSize(resultItemSize - 8)));

        return ret;
    }

    @Override
    public void buildRecipes() {

        buildRecipesCommon();

    }

    protected Ingredient ComponentIngredientOf(ItemStack ingredientStack) {
        return Ingredient.of(ingredientStack.getItem());
    }


//    protected ItemStack[] ingredientItemStacksWithHealth = new ItemStack[3];
//    protected net.klayil.veggycraft.datagen.recipe.ShapedRecipeBuilder[] rawSeitanWithHealthRecipes =
//            new net.klayil.veggycraft.datagen.recipe.ShapedRecipeBuilder[2];


    public void buildRecipesCommon() {
        ItemStack bag;

        shapedLocal(RecipeCategory.FOOD, new ItemStack(ModItems.BROWN_SUGAR))
                .pattern("SB")
                .define('S', ModItemTags.SWORDS)
                .define('B', ModItems.DRIED_MOLASSES.get())
                .unlockedBy("has_dried_mol", has(ModItems.DRIED_MOLASSES.get()))
                .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "brown_sugar_from_bottle")));

        @NotNull
        Ingredient ing = Ingredient.of(Items.WHEAT);
//        Ingredient ing = Ingredient.of(Items.WHEAT.asItem(), Blocks.PISTON);

        PistonSmashRecipeBuilder
            .smash(ing, new ItemStack(ModItems.THIS_MOD_FLOUR))
            .unlockedBy("has_wheat", has(Items.WHEAT))
            .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_smash")));

//        PistonOverWorldRecipeBuilder
//                .smash(ing, new ItemStack(ModItems.THIS_MOD_FLOUR.get()))
//                .unlockedBy("has_wheat", has(Items.WHEAT))
//                // Make sure this method actually passes the Serializer to the RecipeOutput
//                .save(output, String.valueOf(ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "wheat_smash")));

        for (int size = 8; size <= 64; size+=8) {
            bag = flourBagSize(size);

            shapeless(RecipeCategory.MISC, new ItemStack(ModItems.THIS_MOD_FLOUR, 8))
                    .requires(ComponentIngredientOf(bag))
                    .unlockedBy("has_bag_of_%d_flours".formatted(size), has(bag.getItem()))
                    .save(
                            output,
                            "%s:wheat_flour_from_bag_of_%d".formatted(VeggyCraft.MOD_ID, size)
                    );

            if (size < 16) continue;

            flourRecipe(size).unlockedBy("has_before_%d_bag".formatted(size), has(ModItems.THIS_MOD_FLOUR.get())).
                    save(
                            output,
                            "%s:bag_of_%d_crafting_and_advt".formatted(VeggyCraft.MOD_ID, size)
                    );
        }

        shapeless(RecipeCategory.MISC, ModItems.BLACK_DYE_STACK)
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.COAL_CARBON_CUTTER.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(
                        output,
                        "%s:carbon_dye_coal_cut".formatted(VeggyCraft.MOD_ID)
                );

        ItemStack threeBlackDye = ModItems.BLACK_DYE_STACK.copy();
        threeBlackDye.setCount(3);
        shapeless(RecipeCategory.MISC, threeBlackDye)
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.DIAMOND_CARBON_CUTTER.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(
                        output,
                        "%s:carbon_dye_diamon_cut".formatted(VeggyCraft.MOD_ID)
                );

        shapedLocal(RecipeCategory.MISC, new ItemStack(ModItems.BLACK_OF_COAL_CARBON))
                .pattern("##")
                .pattern("##")
                .define('#', ItemTags.COALS)
                //.unlockedBy("has_normal_coal", has(Items.COAL))
                .unlockedBy("has_coal_tag", has(ItemTags.COALS))
                //.unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(output);

        shapeless(RecipeCategory.MISC, new ItemStack(ModItems.SHINY_OF_DIAMOND_COAL_CARBON))
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(ModItems.BLACK_OF_COAL_CARBON.get())
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .requires(Items.DIAMOND)
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get()),
                Ingredient.of(ModItems.COAL_CARBON_CUTTER.get()),
                Ingredient.of(Items.COAL),
                RecipeCategory.TOOLS,
                ModItems.DIAMOND_CARBON_CUTTER.get()
        ).unlocks(
                "has_coal_carbon_cutter",
                has(ModItems.COAL_CARBON_CUTTER.get())
        ).save(output, "%s:carbon_cutter_coal_to_diamond".formatted(VeggyCraft.MOD_ID));

        SmithingTransformRecipeBuilder.smithing(
                Ingredient.of(ModItems.SHINY_OF_DIAMOND_COAL_CARBON.get()),
                Ingredient.of(ModItems.COAL_CARBON_CUTTER.get()),
                Ingredient.of(Items.CHARCOAL),
                RecipeCategory.TOOLS,
                ModItems.DIAMOND_CARBON_CUTTER.get()
        ).unlocks(
                "has_coal_carbon_cutter",
                has(ModItems.COAL_CARBON_CUTTER.get())
        ).save(output, "%s:carbon_cutter_coal_to_diamond_charcoal_addition".formatted(VeggyCraft.MOD_ID));

        shapedLocal(RecipeCategory.MISC, new ItemStack(ModItems.COAL_CARBON_CUTTER))
                .pattern("CBC")
                .pattern(" | ")
                .pattern(" | ")
                .define('C', ItemTags.COALS)
                .define('B', ModItems.BLACK_OF_COAL_CARBON.get())
                .define('|', Items.STICK)
                .unlockedBy("has_black_of_coal_carbon", has(ModItems.BLACK_OF_COAL_CARBON.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.THIS_MOD_FLOUR.get())
                .requires(Items.PISTON)
                .requires(Items.WHEAT)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(output);

        ItemStack drySeitanHealthFirstHealth = null;

        Ingredient ingredient = null;
        for (int health = 0; true; health++) {
            ItemStack drySeitanWithHealth = new ItemStack(ModItems.DRY_RAW_SEITAN_0);
            ItemStack ingredientStack = drySeitanWithHealth.copy();
            ingredientStack.set(
                    ModDataComponentTypes.HEALTH.get(),
                    new ModDataComponentTypes.ItemHealth(health, 3)
            );
            ingredient = ComponentIngredientOf(ingredientStack);

            if (health == 0) {
                drySeitanHealthFirstHealth = ingredientStack;
            }

            if (health > 2) break;

            drySeitanWithHealth.set(
                    ModDataComponentTypes.HEALTH.get(),
                    new ModDataComponentTypes.ItemHealth(health+1, 3)
            );

            shapedLocal(RecipeCategory.MISC, drySeitanWithHealth)
                    .pattern("SB")
                    .define('B', Items.WATER_BUCKET)
                    .unlockedBy("has_water", has(Items.WATER_BUCKET))
                    .define('S', ingredient)
                    .save(output, "%s:raw_seitan_health_%d".formatted(VeggyCraft.MOD_ID, health+1));
        }

        shapedLocal(RecipeCategory.MISC, drySeitanHealthFirstHealth).
                pattern("FB")
                .define('B', Items.WATER_BUCKET)
                .define('F', ModItems.THIS_MOD_FLOUR.get())
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(
                        output
                        //, "%s:raw_seitan_from_flour".formatted(VeggyCraft.MOD_ID)
                );

        assert ingredient != null;
        shaped(RecipeCategory.MISC, ModItems.WET_RAW_SEITAN.get())
                .pattern("SB")
                .define('S', ingredient)
                .define('B', Items.WATER_BUCKET)
                .unlockedBy("has_water", has(Items.WATER_BUCKET))
                .save(output);

        cookingRecipeFood(new ItemStack(ModItems.WET_RAW_SEITAN), ModItems.SEITAN_COOKED_BEEF.get(), 0.35f, 200);

        this.shaped(RecipeCategory.FOOD, Blocks.CAKE)
                .define('A', Items.MILK_BUCKET)
                .define('B', ModItems.BROWN_SUGAR.get())
                .define('C', Items.WHEAT)
                .define('E', ItemTags.EGGS)
                .pattern("AAA")
                .pattern("BEB")
                .pattern("CCC")
                .unlockedBy("has_egg", this.has(ItemTags.EGGS))
                .save(output, "veggycraft:cake_brown_sugar");
        this.shapeless(RecipeCategory.FOOD, Items.PUMPKIN_PIE)
                .requires(Blocks.PUMPKIN)
                .requires(ModItems.BROWN_SUGAR.get())
                .requires(ItemTags.EGGS)
                .unlockedBy("has_carved_pumpkin", this.has(Blocks.CARVED_PUMPKIN))
                .unlockedBy("has_pumpkin", this.has(Blocks.PUMPKIN))
                .save(output, "veggycraft:pie_brown_sugar");

        Item dye;
        String curColor = null;
        Item modalValueI = null;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {

                curColor = ColoursList.listOfColours[i];
                dye = BuiltInRegistries.ITEM.getValue(
                    ResourceLocation.withDefaultNamespace("%s_dye".formatted(curColor))
                );

                modalValueI = ModBlocks.modalFabrics.get(i).get().asItem();


                if (j==i) continue;

                shapeless(RecipeCategory.DECORATIONS, modalValueI)
                    .requires(dye)
                    .requires(ModBlocks.modalFabrics.get(j).get().asItem())
                    .unlockedBy("has_%s_dye".formatted(curColor), has(dye))
                    .save(output, "dying_%s_modal_fabric_%s_block_ingredient".formatted(curColor, ColoursList.listOfColours[j]));
            }
            // noinspection ConstantConditions
            if (modalValueI == null | curColor == null) continue;
            bedAndBannerFromModal(modalValueI, curColor);

            shaped(
                RecipeCategory.DECORATIONS,
                BuiltInRegistries.ITEM.getValue(
                    ResourceLocation.withDefaultNamespace(
                        "%s_carpet".formatted(curColor)
                    )
                )
                ,
          3
            ).pattern("Mn")
            .define('M', modalValueI)
            .define('n', ModItemTags.TAG_MODAL_FABRIC_ITEMS)
            .unlockedBy("has_%s_modal".formatted(curColor), has(modalValueI))
            .save(output, "carpet_from_modal_color_%s".formatted(curColor));


        }



        molassesBlock = ModBlocks.MOLASSES_BLOCK_ITEM.getOrNull();
        if (molassesBlock != null) {

            cookingRecipeFood(new ItemStack(ModItems.SUGAR_BAG), molassesBlock, 0.8f, 20*15);

            hasBl(shapedLocal(RecipeCategory.FOOD, new ItemStack(ModItems.MOLASSES_BOTTLE, 8))
                    .pattern("bbb")
                    .pattern("bMb")
                    .pattern("bbb")
                    .define('b', Items.GLASS_BOTTLE)
                    .define('M', molassesBlock)).save(output);

        }

        shapedLocal(RecipeCategory.FOOD, new ItemStack(ModItems.CHOPPED_APPLE)).pattern("SAB").define('S', ModItemTags.SWORDS)
                .define('A', Items.APPLE)
                .define('B', Items.BOWL)
                .unlockedBy("has_apple", has(Items.APPLE))
                .save(output);


        ItemStack choppedApple = new ItemStack(ModItems.CHOPPED_APPLE);

        SimpleCookingRecipeBuilder.smelting(ComponentIngredientOf(choppedApple), RecipeCategory.FOOD, ModItems.APPLE_SAUCE.get(), 0.5f, 335)
                .unlockedBy("has_chopped", has(choppedApple.getItem()))
                .save(output, "%s_furnace".formatted("apple_sauce"));

        SimpleCookingRecipeBuilder.smoking(ComponentIngredientOf(choppedApple), RecipeCategory.FOOD, ModItems.APPLE_SAUCE.get(), 0.5f, 336 / 2)
                .unlockedBy("has_chopped", has(choppedApple.getItem()))
                .save(output, "%s_smoker".formatted("apple_sauce"));

        ShapelessRecipeBuilder recipeBuilder = shapeless(RecipeCategory.FOOD, new ItemStack(ModItems.SUGAR_BAG))
                .requires(Items.SUGAR, 8);

        hasBl(shapeless(RecipeCategory.FOOD, new ItemStack(ModBlocks.MOLASSES_BLOCK_ITEM))
                .requires(ModItems.MOLASSES_BOTTLE.get(), 8)).save(output);

        Item ice = Items.ICE;

        shapeless(RecipeCategory.FOOD, new ItemStack(ModItems.BROWN_SUGAR, 8))
                .requires(ModItems.MOLASSES_BOTTLE.get(), 4).requires(ice)
                .requires(ModItems.MOLASSES_BOTTLE.get(), 4)
                .unlockedBy("has_ice", has(ice))
                .save(output, ResourceKey.create(Registries.RECIPE, ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, "sugar_brown")));

        for (int i = 1; i < ColoursList.listOfColours.length+1; i++) {
            recipeBuilder = recipeBuilder.unlockedBy("has_bundle_%d".formatted(i), has(
                    BuiltInRegistries.ITEM.getValue(
                            ResourceLocation.withDefaultNamespace(
                                    ColoursList.listOfColours[i-1]+"_bundle"
                            )
                    )
            ));
        }

        recipeBuilder.unlockedBy("has_bundle", has(Items.BUNDLE)).save(output);


        SimpleCookingRecipeBuilder.smelting(ComponentIngredientOf(new ItemStack(ModItems.CARNAUBA_POWDER)),
                        RecipeCategory.MISC, ModItems.CARNAUBA_WAX.get(), 0.3f, 45)
                .unlockedBy("has_power_c", has(new ItemStack(ModItems.CARNAUBA_POWDER).getItem()))
                .save(output);

        TagKey<Item> axes = TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(
                "axes"
        ));

        shapeless(RecipeCategory.BUILDING_BLOCKS, new ItemStack(ModItems.CARNAUBA_POWDER))
                .requires(axes).requires(ModBlocks.CARNAUBA_WOODS.get("leaves").get().asItem())
                .unlockedBy("has_an_axe", has(axes))
                .save(output);
    }

    ItemLike molassesBlock;
    int hasMolassesBlockIndex = 1;
    public RecipeBuilder hasBl(RecipeBuilder recp) {
        recp = recp.unlockedBy("has_block_m"+hasMolassesBlockIndex, has(molassesBlock));

        hasMolassesBlockIndex++;

        return recp;
    }

//    boolean whiteBedRecipeDone = false;

    final Item WHITE_MODAL_FABRIC_BLOCK = ModBlocks.modalFabrics.getFirst().get().asItem();

    private void bedAndBannerFromModal(ItemLike _modal, String colorText) {
//        if (whiteBedRecipeDone) return;

        assert holderGetterProvider != null;

        final HolderGetter<Item> itemGetter = holderGetterProvider.lookupOrThrow(Registries.ITEM);

        Item modal = _modal.asItem();

        int atLeast = 3;
        String patternTop = "MMM";
        boolean isWhite = false;

        if (modal.asItem() == ModBlocks.modalFabrics.getFirst().get().asItem()) {
            atLeast = 1;
            patternTop = "WMM";
            isWhite = true;
        }


        ShapedRecipeBuilder bedRecipe = shapedLocal(RecipeCategory.DECORATIONS, new ItemStack(
                BuiltInRegistries.ITEM.getValue(
                        ResourceLocation.withDefaultNamespace("%s_bed".formatted(colorText))
                )
        ));

        ShapedRecipeBuilder banner = shapedLocal(RecipeCategory.DECORATIONS, new ItemStack(
                BuiltInRegistries.ITEM.getValue(
                        ResourceLocation.withDefaultNamespace("%s_banner".formatted(colorText))
                )
        ));

        banner = banner.pattern(patternTop)
        .pattern("MMM")
        .pattern(" | ")
        .define('|', Items.STICK);

        bedRecipe = bedRecipe.pattern(patternTop)
        .pattern("PPP")
        .define('P', ItemTags.PLANKS);

        if (isWhite) {
            bedRecipe = bedRecipe
                       .define('M', ModItemTags.TAG_MODAL_FABRIC_ITEMS)
                       .define('W', WHITE_MODAL_FABRIC_BLOCK);

            banner = banner
                    .define('M', ModItemTags.TAG_MODAL_FABRIC_ITEMS)
                    .define('W', WHITE_MODAL_FABRIC_BLOCK);
        } else {
            bedRecipe = bedRecipe.define('M', modal);

            banner = banner.define('M', modal);
        }

        bedRecipe.unlockedBy(
            "has_three_%s_modals".formatted(colorText),
            CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                new InventoryChangeTrigger.TriggerInstance(
                    Optional.empty(),
                    InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                    List.of(
                        ItemPredicate.Builder.item()
                            .of(
                                itemGetter,
                                modal
                            )
                            .withCount(MinMaxBounds.Ints.atLeast(atLeast))
                            .build()
                    )
                )
            )
        ).save(output, "%s_bed_with_modal".formatted(colorText/*, Math.abs(new Random().nextInt())*/));

        banner.unlockedBy(
                "has_six_%s_modals".formatted(colorText),
                CriteriaTriggers.INVENTORY_CHANGED.createCriterion(
                        new InventoryChangeTrigger.TriggerInstance(
                                Optional.empty(),
                                InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                                List.of(
                                        ItemPredicate.Builder.item()
                                                .of(
                                                        itemGetter,
                                                        modal
                                                )
                                                .withCount(MinMaxBounds.Ints.atLeast((int) Math.pow(atLeast, 1.631)))
                                                .build()
                                )
                        )
                )
        ).save(output, "%s_banner_with_modal".formatted(colorText));
    }

    private void cookingRecipeFood(ItemStack ingredientStack, ItemLike result, float experience, int cookingTime) {
        String hasIngredientText = "has_%s".formatted(
                BuiltInRegistries.ITEM.getKey(ingredientStack.getItem()).getPath()
        );

        String resultName = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();

        SimpleCookingRecipeBuilder.smelting(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_furnace".formatted(resultName));

        SimpleCookingRecipeBuilder.smoking(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime / 2)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_smoker".formatted(resultName));

        SimpleCookingRecipeBuilder.campfireCooking(ComponentIngredientOf(ingredientStack), RecipeCategory.FOOD, result, experience, cookingTime * 3)
                .unlockedBy(hasIngredientText, has(ingredientStack.getItem()))
                .save(output, "%s_campfire".formatted(resultName));
    }
}
