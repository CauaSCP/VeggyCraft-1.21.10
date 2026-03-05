package net.klayil.veggycraft.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.datagen.ColoursList;
import net.klayil.veggycraft.item.tabs.CustomTabsMethods;
import net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.food.FoodProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class ModItems {
//    private static String currentItemName;

    public static RegistrySupplier<Item> COAL_CARBON_CUTTER;
    public static RegistrySupplier<Item> DIAMOND_CARBON_CUTTER;
    public static RegistrySupplier<Item> SHINY_OF_DIAMOND_COAL_CARBON;

    public static RegistrySupplier<Item> THIS_MOD_CLOCK;

    public static ArrayList<RegistrySupplier<Item>> modalFabricItems = new ArrayList<>();

    public static String waxID = "carnauba_wax";
    public static RegistrySupplier<Item> CARNAUBA_WAX;
    public static RegistrySupplier<Item> CARNAUBA_POWDER;

    public static RegistrySupplier<Item> THIS_MOD_FLOUR;
    public static RegistrySupplier<Item> SEITAN_COOKED_BEEF;
    public static RegistrySupplier<Item> WET_RAW_SEITAN;
    public static RegistrySupplier<Item> DRY_RAW_SEITAN_0;
//    public static RegistrySupplier<Item> DRY_RAW_SEITAN_1;
//    public static RegistrySupplier<Item> DRY_RAW_SEITAN_2;
    public static RegistrySupplier<Item> BLACK_OF_COAL_CARBON;

    public static RegistrySupplier<Item> FLOUR_BAG;

    public static ItemStack BLACK_DYE_STACK;

    public static RegistrySupplier[] VeggyMeats;

    public static RegistrySupplier<Item> BROWN_SUGAR;
    public static RegistrySupplier<Item> MOLASSES_BOTTLE;
    public static RegistrySupplier<Item> DRIED_MOLASSES;

    public static RegistrySupplier<Item> APPLE_SAUCE;
    public static RegistrySupplier<Item> CHOPPED_APPLE;

    public static RegistrySupplier<Item> SUGAR_BAG;

    public static RegistrySupplier<Item> EVEN_STRIPPED_BIRCH_LOG;
    public static RegistrySupplier<Item> STRAW_BED;
    public static RegistrySupplier<Item> BIRCH_PULP;

    public static RegistrySupplier<Item> ALGAE_EXTRACT;

    public static ArrayList<ItemStack> MUNDANE_SPLASH_POTION_ITEM_STACKS = new ArrayList<>();
    public static RegistrySupplier<SplashPotionItem> OTHER_SPLASH_POTION;
    public static int OTHER_SPLASH_POTION_COLOR = 0xd4d4d5;

    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(VeggyCraft.MOD_ID, Registries.POTION);
    public static DeferredRegister<Item> ITEMS = KlayApiModItems.createItemsRegister(VeggyCraft.MOD_ID);

    private static RegistrySupplier<Item> createBlockItemWithCustomName(String itemId, Component nameToTranslate, RegistrySupplier<Block> block, int maxStackSize) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemId);

        @Nullable
        ResourceKey<CreativeModeTab> tabRemove = null;
        int remove_index = -1;


        for (ResourceKey<CreativeModeTab> curTab : KlayApiModBlocks.blockItemCreativeModeTabs.keySet()) {
            if (!(KlayApiModBlocks.blockItemCreativeModeTabs.get(curTab)).isEmpty()) {
                ArrayList<String> arr =KlayApiModBlocks.blockItemCreativeModeTabs.get(curTab);

                for (int index = 0; index < arr.size(); ++index) {
                    ResourceLocation blockLocation = ResourceLocation.parse(arr.get(index));

                    RegistrySupplier<Block> blockOfLoop = (KlayApiModBlocks.AllKlayApiBlocks.get(blockLocation.toString()));

                    if (blockOfLoop == null) continue;
                    if (blockOfLoop.getKey().location() == block.getKey().location()) {
                        remove_index = index;
                        tabRemove = curTab;

                        break;
                    }
                }
            }
        }


        if (remove_index >= 0) {
            KlayApiModBlocks.blockItemCreativeModeTabs.get(tabRemove).remove(remove_index);
        }

        RegistrySupplier<Item> item = ITEMS.register(
                itemLocation,
                () -> new BlockItemWithCustomName(
                        KlayApiModItems.baseProperties(itemId, VeggyCraft.MOD_ID).stacksTo(maxStackSize),
                        nameToTranslate,
                        block
                )
        );

        if (remove_index >= 0) {
            if (tabRemove != null & item != null) CreativeTabRegistry.append(tabRemove, item);
        }

        return item;
    }

    private static RegistrySupplier<Item> createDamageableItem(String itemId, int maxStackSize, int maxDamage, @Nullable ResourceKey<CreativeModeTab> creativeModeTab, @Nullable Supplier<Item>... repairItemViaList) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemId);

        @Nullable Supplier<Item> repairItem;
        RegistrySupplier<Item> registeredItem;

        if (repairItemViaList.length >= 1) {
            assert repairItemViaList[0] != null;
            VeggyCraft.LOGGER.info("#SUP: " + repairItemViaList[0]);

            repairItem = repairItemViaList[0];

            @Nullable Supplier<Item> finalRepairItem = repairItem;

            registeredItem = ITEMS.register(
                itemLocation,
                () -> new RepairableItemsExtension(KlayApiModItems.baseProperties(itemId, VeggyCraft.MOD_ID), maxDamage, () -> finalRepairItem)
            );
        } else {
            repairItem = null;

            registeredItem = ITEMS.register(
                    itemLocation,
                    () -> new UnrepairableDamageableItems(
                            KlayApiModItems.baseProperties(itemId, VeggyCraft.MOD_ID).stacksTo(maxStackSize)
                            //.craftRemainder(Items.BLUE_STAINED_GLASS_PANE)
                            ,
                            maxDamage
                    )
            );
        }

        KlayApiModItems.AllKlayApiItems.put(itemLocation.toString(), registeredItem);

        if (creativeModeTab != null) {
            CreativeTabRegistry.append(creativeModeTab, registeredItem); // probably here is making the error possible
        }

        return registeredItem;
    }

    private static Item.Properties honeyBottleProps(String id, String ignored) {
        return (new Item.Properties()).craftRemainder(Items.GLASS_BOTTLE)
                .food(Foods.HONEY_BOTTLE, Consumables.HONEY_BOTTLE)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .stacksTo(16).setId(ResourceKey.create(
                        Registries.ITEM,
                        ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, id)
                ));
    }

    public static void initItems() {
        String m = VeggyCraft.MOD_ID;

//        KlayApiModItems.initItems();

        BLACK_DYE_STACK = new ItemStack(Items.BLACK_DYE);
        CustomModelData data = new CustomModelData(
                List.of(),
                List.of(),
                List.of("carbon_black_dye"),
                List.of()
        );

//        ALGAE_EXTRACT = POTIONS.register("algae_extract", () -> new Potion("algae_extract", new MobEffectInstance(ALGAE_EXTRACT_COLOR, 0, 0, false, true, false)));

        String e = "algae_extract";
        final FoodProperties ALGAE_EXTRACT_PROPS = new FoodProperties(0 , 0, true);
        ALGAE_EXTRACT = KlayApiModItems.createItem(e, null, () -> KlayApiModItems.baseProperties(e, m).craftRemainder(Items.GLASS_BOTTLE)
                .food(ALGAE_EXTRACT_PROPS, Consumables.defaultDrink().build())
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .stacksTo(1),
        m);

        CustomTabsMethods.addToTab(CreativeModeTabs.FOOD_AND_DRINKS, CustomTabsMethods.BEFORE, ALGAE_EXTRACT, Items.SPLASH_POTION);

//        Holder<Potion> potionHolder = POTIONS.register("placeholder_effected", () -> new Potion("placeholder_effected", new MobEffectInstance(ModEffects.PLACEHOLDER_EFFECT, 0, 0, true, true, false)));

//        VeggyCraft.LOGGER.info("#Potion: parsed %s", potionHolder);

        String potion_name = "splash_potion_2";
        OTHER_SPLASH_POTION = ITEMS.register(potion_name, () -> new SplashPotionItem(KlayApiModItems.baseProperties(potion_name, m).stacksTo(1).component(DataComponents.POTION_CONTENTS, new PotionContents(Optional.empty(), Optional.of(OTHER_SPLASH_POTION_COLOR),  List.of(), Optional.empty()))));

//        VeggyCraft.LOGGER.info("#Potion: parsed %s", splashPotionHolder);

        CustomTabsMethods.addToTab(CreativeModeTabs.FOOD_AND_DRINKS, CustomTabsMethods.BEFORE, (RegistrySupplier) OTHER_SPLASH_POTION, Items.LINGERING_POTION);
//        CreativeTabRegistry.append(VeggyCraftCreativeTabsToGet.REPLACEMENTS, OTHER_SPLASH_POTION);

        MutableComponent dyeName = Component.translatable("item.veggycraft.carbon_prefix")
                .append(Component.translatable("item.veggycraft.carbon_space_1"))
                .append(Component.translatable("item.minecraft.black_dye"))
                .append(Component.translatable("item.veggycraft.carbon_space_2"))
                .append(Component.translatable("item.veggycraft.carbon_suffix"));



//        ITEMS.register("clock_2_0", () -> new ClockItem);

        var n = "brown_sugar_in_bottle";
        DRIED_MOLASSES = KlayApiModItems.createItem(
                n,
 null,
                () -> KlayApiModItems.baseProperties(n, m).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE),
                m
        );

        var pulp = "birch_pulp_modal";
        BIRCH_PULP = KlayApiModItems.createItem(pulp, null, m);

        var b = "straw_bed";
        STRAW_BED = ITEMS.register(b, () -> new BlockItem(ModBlocks.STRAW_BED.get(), KlayApiModItems.baseProperties(b, m).stacksTo(1)));

        var l = "even_stripped_birch_log";
        EVEN_STRIPPED_BIRCH_LOG = ITEMS.register(l, () -> new BlockItem(ModBlocks.EVEN_STRIPPED_BIRCH_LOG.get(), KlayApiModItems.baseProperties(l, m)));
        CustomTabsMethods.addToTab(CreativeModeTabs.BUILDING_BLOCKS, CustomTabsMethods.AFTER, EVEN_STRIPPED_BIRCH_LOG, Items.BIRCH_FENCE_GATE);

        BROWN_SUGAR = KlayApiModItems.createItem("brown_sugar", null, m);
        MOLASSES_BOTTLE = KlayApiModItems.createItem("molasses_bottle", null, ModItems::honeyBottleProps, m);

        BLACK_DYE_STACK.set(DataComponents.CUSTOM_MODEL_DATA, data);
        BLACK_DYE_STACK.set(DataComponents.ITEM_NAME, dyeName);

        SHINY_OF_DIAMOND_COAL_CARBON = KlayApiModItems.createItem("shiny_of_diamond_coal_carbon", CreativeModeTabs.INGREDIENTS, m);

        final String flourName = "wheat_flour";
        THIS_MOD_FLOUR = KlayApiModItems.createItem(flourName, null, () -> KlayApiModItems.baseProperties(flourName, m).stacksTo(8), m);

        var p = "carnauba_powder";
        CARNAUBA_POWDER = KlayApiModItems.createItem(p, null, () -> KlayApiModItems.baseProperties(p, m), m);
        CARNAUBA_WAX = KlayApiModItems.createItem(waxID, null, () -> KlayApiModItems.baseProperties(waxID, m), m);

        final FoodProperties SEITAN_COOKED_BEEF_PROPS = new FoodProperties(5 * 2, 7 * 2, false);
        SEITAN_COOKED_BEEF = KlayApiModItems.createItem("seitan_cooked_beef", null, () -> KlayApiModItems.baseProperties("seitan_cooked_beef", m).food(SEITAN_COOKED_BEEF_PROPS), m);

//        currentItemName = "08_items_stacked_of_flour";
        FLOUR_BAG = createDamageableItem("items_stacked_of_flour", 1, 64, null);

//        for (int index = 16; index <= 64; index += 8) {
//            currentItemName = "%d_items_stacked_of_flour".formatted(index);
//
//            createDamageableItem(currentItemName, 1, 64, null);
//        }


        BLACK_OF_COAL_CARBON = KlayApiModItems.createItem("black_of_coal_carbon", null, m);

        COAL_CARBON_CUTTER = createDamageableItem("coal_carbon_cutter", 1, 23, VeggyCraftCreativeTabsToGet.CARBON_AND_DYES_TAB, () -> BLACK_OF_COAL_CARBON.get());
        DIAMOND_CARBON_CUTTER = createDamageableItem("diamond_carbon_cutter", 1, 813, VeggyCraftCreativeTabsToGet.CARBON_AND_DYES_TAB, () -> Items.DIAMOND);


        DRY_RAW_SEITAN_0 = createDamageableItem("dry_raw_seitan", 1, 3, null);
//        DRY_RAW_SEITAN_1 = createDamageableItem("dry_raw_seitan_1", 1, 3, null);
//        DRY_RAW_SEITAN_2 = createDamageableItem("dry_raw_seitan_2", 1, 3, null);
        WET_RAW_SEITAN = KlayApiModItems.createItem("wet_raw_seitan", null, m);

        VeggyMeats = new RegistrySupplier[]{SEITAN_COOKED_BEEF};

//        int index = 0;

        for (int i = 0; i < ModBlocks.modalFabrics.size(); i++) {
            RegistrySupplier<Block> curModal = ModBlocks.modalFabrics.get(i);
            RegistrySupplier<Item> curItem = createBlockItemWithCustomName(
                    curModal.getKey().location().getPath() //"%s_item".formatted(curModal.getKey().location().getPath())
                    ,
                    Component.translatable("item.veggycraft.modal")
                            .append(Component.translatable("color.minecraft.%s".formatted(ColoursList.listOfColours[i])))
                            .append(Component.translatable("suffix.veggycraft.modal")),
                    curModal,
                    64
            );

            modalFabricItems.add(curItem);

            CustomTabsMethods.addToTab(CreativeModeTabs.COLORED_BLOCKS, CustomTabsMethods.BEFORE, curItem, Items.GLASS);
//

//            createDamageableItem("%s_item".formatted(curModal.getKey().location().getPath()), 64, 1000, CreativeModeTabs.INGREDIENTS);
        }


        KlayApiModItems.createItemsOfBlocks();



//        RegisteredBlocks<Block> registeredBlocks = (RegisteredBlocks<Block>) ModBlocks.BLOCKS;
//
//        Boolean registered = registeredBlocks.registered();
//
//        if (registered) {
//
//            StreamSupport.stream(registeredBlocks.spliterator(), true).forEach((registrySupplier -> {
//                registrySupplier.unwrapKey().ifPresent(blockResourceKey -> {
//                    VeggyCraft.LOGGER.warn("#Block: %s", blockResourceKey.location());
//                });
//                return registrySupplier;
//            }));
//        }


//        VeggyCraft.LOGGER.warn("#Registered: %s", );

        ModBlocks.CARNAUBA_WOODS.get("planks").unwrapKey().ifPresent(
                resKey -> {
//                    VeggyCraft.LOGGER.warn("#PlanksBlock: %s", KlayApiModItems.AllKlayApiItems.get(resKey.location().toString()));

                    CustomTabsMethods.addToTab(
                            CreativeModeTabs.BUILDING_BLOCKS,
                            CustomTabsMethods.BEFORE,
                            KlayApiModItems.AllKlayApiItems.get(
                                    resKey.location().toString()
                            ),
                            Items.CRIMSON_STEM
                    );
                }
        );

        String a = "apple";
        String c = "chopped_";
        final String s = "_sauce";

        APPLE_SAUCE = KlayApiModItems.createItem(a+s, null, ()-> KlayApiModItems.baseProperties(a+s, m), m);
        CHOPPED_APPLE = KlayApiModItems.createItem(c+a, null, ()-> KlayApiModItems.baseProperties(c+a, m).usingConvertsTo(Items.BOWL).food(Foods.APPLE), m);

        String sug = "sugar_bag";

        SUGAR_BAG = KlayApiModItems.createItem(sug, null, () -> KlayApiModItems.baseProperties(sug, m).stacksTo(8), m);
        CustomTabsMethods.addToTab(CreativeModeTabs.INGREDIENTS, CustomTabsMethods.AFTER, SUGAR_BAG, Items.SUGAR);

        THIS_MOD_CLOCK = KlayApiModItems.createItem("my_clock", null, m);

//        __BEFORE = KlayApiModItems.createItem("__replacements_icon__", null, m);

        ITEMS.register();


//        Item[] test =



//        VeggyCraft.myRecipesStacks.add(
//                new ItemStack[] {
//                        waterBottle,
//                        new ItemStack(ALGAE_EXTRACT.get()),
//                        new ItemStack(Items.KELP)
//                }
//        );
//

//        POTIONS.register();

//        BEFORE = __BEFORE.getOrNull();

//        CustomTabsMethods.addToTab(VeggyCraftCreativeTabsToGet.REPLACEMENTS, __BEFORE, BEFORE);
    }
}
