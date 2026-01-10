package net.klayil.veggycraft.item;

import com.mojang.serialization.DataResult;
import dev.architectury.registry.registries.RegistrarManager;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.klayil.klay_api.KlayApi;
import net.klayil.klay_api.block.KlayApiModBlocks;
import net.klayil.klay_api.item.KlayApiModItems;
import net.klayil.veggycraft.VeggyCraft;
import net.klayil.veggycraft.block.ModBlocks;
import net.klayil.veggycraft.datagen.ColoursList;
import net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.food.FoodProperties;


import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static net.klayil.klay_api.item.KlayApiModItems.AllKlayApiItems;
import static net.klayil.klay_api.item.KlayApiModItems.baseProperties;
import static net.klayil.veggycraft.item.tabs.VeggyCraftCreativeTabsToGet.REPLACEMENTS;

import net.minecraft.core.Holder;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class ModItems {
    private static String currentItemName;

    public static RegistrySupplier<Item> COAL_CARBON_CUTTER;
    public static RegistrySupplier<Item> DIAMOND_CARBON_CUTTER;
    public static RegistrySupplier<Item> SHINY_OF_DIAMOND_COAL_CARBON;

    public static ArrayList<RegistrySupplier<Item>> modalFabricItems = new ArrayList<>();


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


//    Items.POTION

    public static DeferredRegister<Item> ITEMS = KlayApiModItems.createItemsRegister(VeggyCraft.MOD_ID);

    private static RegistrySupplier<Item> createBlockItemWithCustomName(String itemId, Component nameToTranslate, RegistrySupplier<Block> block, int maxStackSize) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemId);

        @Nullable
        ResourceKey<CreativeModeTab> tabRemove = null;
        int remove_index = -1;


        for (ResourceKey<CreativeModeTab> curTab : KlayApiModBlocks.blockItemCreativeModeTabs.keySet()) {
            if (!((ArrayList) KlayApiModBlocks.blockItemCreativeModeTabs.get(curTab)).isEmpty()) {
                ArrayList<String> arr = (ArrayList) KlayApiModBlocks.blockItemCreativeModeTabs.get(curTab);

                for(int index = 0; index < arr.size(); ++index) {
                    ResourceLocation blockLocation = ResourceLocation.parse((String)arr.get(index));

                    RegistrySupplier<Block> blockOfLoop = ((RegistrySupplier)KlayApiModBlocks.AllKlayApiBlocks.get(blockLocation.toString()));


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
            CreativeTabRegistry.append(tabRemove, item);
        }

        return item;
    }

    private static RegistrySupplier<Item> createDamageableItem(String itemId, int maxStackSize, int maxDamage, @Nullable ResourceKey<CreativeModeTab> creativeModeTab, @Nullable Supplier<Item>... repairItemViaList) {
        ResourceLocation itemLocation = ResourceLocation.fromNamespaceAndPath(VeggyCraft.MOD_ID, itemId);

        @Nullable Supplier<Item> repairItem;
        RegistrySupplier<Item> registeredItem;

        if (repairItemViaList.length >= 1) {
            assert repairItemViaList[0] != null;
            VeggyCraft.LOGGER.info("#SUP: " + repairItemViaList[0].toString());

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

        AllKlayApiItems.put(itemLocation.toString(), registeredItem);

        if (creativeModeTab != null) {
            CreativeTabRegistry.append(creativeModeTab, registeredItem); // probably here is making the error possible
        }

        return registeredItem;
    }

    public static void initItems() {
//        KlayApiModItems.initItems();

        BLACK_DYE_STACK = new ItemStack(Items.BLACK_DYE);
        CustomModelData data = new CustomModelData(
                List.of(),
                List.of(),
                List.of("carbon_black_dye"),
                List.of()
        );

        MutableComponent dyeName = Component.translatable("item.veggycraft.carbon_prefix")
                .append(Component.translatable("item.veggycraft.carbon_space_1"))
                .append(Component.translatable("item.minecraft.black_dye"))
                .append(Component.translatable("item.veggycraft.carbon_space_2"))
                .append(Component.translatable("item.veggycraft.carbon_suffix"));


        BLACK_DYE_STACK.set(DataComponents.CUSTOM_MODEL_DATA, data);
        BLACK_DYE_STACK.set(DataComponents.ITEM_NAME, dyeName);

        SHINY_OF_DIAMOND_COAL_CARBON = KlayApiModItems.createItem("shiny_of_diamond_coal_carbon", CreativeModeTabs.INGREDIENTS, VeggyCraft.MOD_ID);

        final String flourName = "wheat_flour";
        THIS_MOD_FLOUR = KlayApiModItems.createItem(flourName, null, () -> KlayApiModItems.baseProperties(flourName, VeggyCraft.MOD_ID).stacksTo(8), VeggyCraft.MOD_ID);

        final FoodProperties SEITAN_COOKED_BEEF_PROPS = new FoodProperties(5 * 2, 7 * 2, false);
        SEITAN_COOKED_BEEF = KlayApiModItems.createItem("seitan_cooked_beef", null, () -> KlayApiModItems.baseProperties("seitan_cooked_beef", VeggyCraft.MOD_ID).food(SEITAN_COOKED_BEEF_PROPS), VeggyCraft.MOD_ID);

//        currentItemName = "08_items_stacked_of_flour";
        FLOUR_BAG = createDamageableItem("items_stacked_of_flour", 1, 64, null);

//        for (int index = 16; index <= 64; index += 8) {
//            currentItemName = "%d_items_stacked_of_flour".formatted(index);
//
//            createDamageableItem(currentItemName, 1, 64, null);
//        }


        BLACK_OF_COAL_CARBON = KlayApiModItems.createItem("black_of_coal_carbon", null, VeggyCraft.MOD_ID);

        COAL_CARBON_CUTTER = createDamageableItem("coal_carbon_cutter", 1, 23, VeggyCraftCreativeTabsToGet.CARBON_AND_DYES_TAB, () -> BLACK_OF_COAL_CARBON.get());
        DIAMOND_CARBON_CUTTER = createDamageableItem("diamond_carbon_cutter", 1, 813, VeggyCraftCreativeTabsToGet.CARBON_AND_DYES_TAB, () -> Items.DIAMOND);


        DRY_RAW_SEITAN_0 = createDamageableItem("dry_raw_seitan", 1, 3, null);
//        DRY_RAW_SEITAN_1 = createDamageableItem("dry_raw_seitan_1", 1, 3, null);
//        DRY_RAW_SEITAN_2 = createDamageableItem("dry_raw_seitan_2", 1, 3, null);
        WET_RAW_SEITAN = KlayApiModItems.createItem("wet_raw_seitan", null, VeggyCraft.MOD_ID);

        VeggyMeats = new RegistrySupplier[]{SEITAN_COOKED_BEEF};

        int index = 0;

        for (RegistrySupplier<Block> curModal : ModBlocks.modalFabrics) {
            RegistrySupplier<Item> curItem = createBlockItemWithCustomName(
                    curModal.getKey().location().getPath() //"%s_item".formatted(curModal.getKey().location().getPath())
                    ,
                    Component.translatable("item.veggycraft.modal")
                            .append(Component.literal(" "))
                            .append(Component.translatable("color.minecraft.%s".formatted(ColoursList.listOfColours[index])))
                            .append(Component.translatable("suffix.veggycraft.modal")),
                    curModal,
                    64
            );

            modalFabricItems.add(curItem);
//

//            createDamageableItem("%s_item".formatted(curModal.getKey().location().getPath()), 64, 1000, CreativeModeTabs.INGREDIENTS);
            index++;
        }

        KlayApiModItems.createItemsOfBlocks();

        ITEMS.register();
    }
}
